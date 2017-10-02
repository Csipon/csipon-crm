package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.ComplaintDao;
import com.csipon.crm.dao.HistoryDao;
import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dao.impl.sql.HistorySqlQuery;
import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.proxy.ComplaintProxy;
import com.csipon.crm.domain.proxy.OrderProxy;
import com.csipon.crm.dto.GraphDto;
import com.netcracker.crm.dao.*;
import com.netcracker.crm.domain.model.*;
import com.csipon.crm.domain.proxy.ProductProxy;
import com.csipon.crm.domain.real.RealHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author YARUS
 */
@Repository
public class HistoryDaoImpl implements HistoryDao {
    private static final Logger log = LoggerFactory.getLogger(HistoryDaoImpl.class);

    private ComplaintDao complaintDao;
    private OrderDao orderDao;
    private ProductDao productDao;

    private SimpleJdbcInsert historyInsert;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private HistoryWithDetailExtractor historyWithDetailExtractor;

    @Autowired
    public HistoryDaoImpl(ComplaintDao complaintDao, OrderDao orderDao, ProductDao productDao) {
        this.complaintDao = complaintDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.historyInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(HistorySqlQuery.PARAM_HISTORY_TABLE)
                .usingGeneratedKeyColumns(HistorySqlQuery.PARAM_HISTORY_ID);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.historyWithDetailExtractor = new HistoryWithDetailExtractor(complaintDao, orderDao, productDao);
    }

    @Override
    public Long create(History history) {
        if (history.getId() != null) {
            return null;
        }
        Long orderId = getOrderId(history.getOrder());
        Long complaintId = getComplaintId(history.getComplaint());
        Long productId = getProductId(history.getProduct());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_HISTORY_DATE_CHANGE_STATUS, history.getDateChangeStatus())
                .addValue(HistorySqlQuery.PARAM_HISTORY_DESC_CHANGE_STATUS, history.getDescChangeStatus())
                .addValue(HistorySqlQuery.PARAM_HISTORY_NEW_STATUS_ID, history.getNewStatus().getId())
                .addValue(HistorySqlQuery.PARAM_HISTORY_ORDER_ID, orderId)
                .addValue(HistorySqlQuery.PARAM_HISTORY_COMPLAINT_ID, complaintId)
                .addValue(HistorySqlQuery.PARAM_HISTORY_PRODUCT_ID, productId);

        long id = historyInsert.executeAndReturnKey(params).longValue();
        history.setId(id);
        log.info("History with id: " + id + " is successfully created.");
        return id;
    }

    private Long getOrderId(Order order) {
        if (order != null) {
            return order.getId();
        }
        return null;
    }

    private Long getComplaintId(Complaint complaint) {
        if (complaint != null) {
            return complaint.getId();
        }
        return null;
    }

    private Long getProductId(Product product) {
        if (product != null) {
            return product.getId();
        }
        return null;
    }

    @Override
    public Long update(History history) {
        Long historyId = history.getId();
        if (historyId == null) {
            return null;
        }
        Long orderId = getOrderId(history.getOrder());
        Long complaintId = getComplaintId(history.getComplaint());
        Long productId = getProductId(history.getProduct());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_HISTORY_ID, historyId)
                .addValue(HistorySqlQuery.PARAM_HISTORY_DATE_CHANGE_STATUS, history.getDateChangeStatus())
                .addValue(HistorySqlQuery.PARAM_HISTORY_DESC_CHANGE_STATUS, history.getDescChangeStatus())
                .addValue(HistorySqlQuery.PARAM_HISTORY_NEW_STATUS_ID, history.getNewStatus().getId())
                .addValue(HistorySqlQuery.PARAM_HISTORY_ORDER_ID, orderId)
                .addValue(HistorySqlQuery.PARAM_HISTORY_COMPLAINT_ID, complaintId)
                .addValue(HistorySqlQuery.PARAM_HISTORY_PRODUCT_ID, productId);

        int updatedRows = namedJdbcTemplate.update(HistorySqlQuery.SQL_UPDATE_HISTORY, params);

        if (updatedRows > 0) {
            log.info("History with id: " + historyId + " is successfully updated.");
            return historyId;
        } else {
            log.error("History was not updated.");
            return null;
        }
    }

    @Override
    public Long delete(Long id) {
        if (id != null) {
            SqlParameterSource params = new MapSqlParameterSource().addValue(HistorySqlQuery.PARAM_HISTORY_ID, id);

            long affectedRows = namedJdbcTemplate.update(HistorySqlQuery.SQL_DELETE_HISTORY, params);

            if (affectedRows > 0) {
                log.info("History with id: " + id + " is successfully deleted.");
                return affectedRows;
            } else {
                log.error("History doesn't deleted.");
                return affectedRows;
            }
        }
        return null;
    }

    @Override
    public Long delete(History history) {
        if (history != null) {
            return delete(history.getId());
        }
        return null;
    }

    @Override
    public History findById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_HISTORY_ID, id);
        List<History> allHistory = namedJdbcTemplate.query(HistorySqlQuery.SQL_FIND_HISTORY_BY_ID, params, historyWithDetailExtractor);
        History history = null;
        if (allHistory.size() != 0) {
            history = allHistory.get(0);
        }
        return history;
    }

    @Override
    public List<History> findAllByDate(LocalDate date) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_HISTORY_DATE_CHANGE_STATUS, date);
        return namedJdbcTemplate.query(HistorySqlQuery.SQL_FIND_ALL_HISTORY_BY_DATE, params, historyWithDetailExtractor);
    }

    @Override
    public List<History> findAllByOrderId(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_HISTORY_ORDER_ID, id);
        return namedJdbcTemplate.query(HistorySqlQuery.SQL_FIND_ALL_HISTORY_BY_ORDER_ID, params, historyWithDetailExtractor);
    }

    @Override
    public List<History> findAllByComplaintId(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_HISTORY_COMPLAINT_ID, id);
        return namedJdbcTemplate.query(HistorySqlQuery.SQL_FIND_ALL_HISTORY_BY_COMPLAINT_ID, params, historyWithDetailExtractor);
    }

    @Override
    public List<History> findAllByProductId(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_HISTORY_PRODUCT_ID, id);
        return namedJdbcTemplate.query(HistorySqlQuery.SQL_FIND_ALL_HISTORY_BY_PRODUCT_ID, params, historyWithDetailExtractor);
    }

    @Override
    public GraphDto findComplaintHistoryBetweenDateChangeByProductIds(LocalDate fromDate, LocalDate toDate, GraphDto graphDto) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_GRAPH_FROM_DATE, fromDate)
                .addValue(HistorySqlQuery.PARAM_GRAPH_TO_DATE, toDate);

        String beginSql = HistorySqlQuery.BEGIN_SQL_GRAPH_FOR_COMPLAINTS;
        if (graphDto.getElementIds().isEmpty()) {
            beginSql = HistorySqlQuery.BEGIN_SQL_GRAPH_FOR_COMPLAINTS_FOR_ALL_PRODUCTS;
        }

        String sql = createSqlBetweenDateChangeAndProductIds(beginSql, graphDto, ComplaintStatus.OPEN.getId());
        return namedJdbcTemplate.query(sql, params, new GraphExtractor(graphDto, fromDate, toDate));
    }

    @Override
    public GraphDto findOrderHistoryBetweenDateChangeByProductIds(LocalDate fromDate, LocalDate toDate, GraphDto graphDto) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(HistorySqlQuery.PARAM_GRAPH_FROM_DATE, fromDate)
                .addValue(HistorySqlQuery.PARAM_GRAPH_TO_DATE, toDate);

        String beginSql = HistorySqlQuery.BEGIN_SQL_GRAPH_FOR_ORDER;
        if (graphDto.getElementIds().isEmpty()) {
            beginSql = HistorySqlQuery.BEGIN_SQL_GRAPH_FOR_ORDER_FOR_ALL_PRODUCTS;
        }

        String sql = createSqlBetweenDateChangeAndProductIds(beginSql, graphDto, OrderStatus.NEW.getId());
        return namedJdbcTemplate.query(sql, params, new GraphExtractor(graphDto, fromDate, toDate));
    }

    private String createSqlBetweenDateChangeAndProductIds(String beginSgl, GraphDto graphDto, Long statusId) {
        StringBuilder stringBuilder = new StringBuilder();
        beginSgl = beginSgl.replace(HistorySqlQuery.PARAM_GRAPH_TYPE_DATE_CHANGE, graphDto.getTypeDateChange());
        stringBuilder.append(beginSgl);

        for (int i = 0; i < graphDto.getElementIds().size(); i++) {
            if (i == 0) {
                stringBuilder.append(" AND ( ");
            }
            stringBuilder.append(" o.product_id = ");
            stringBuilder.append(graphDto.getElementIds().get(i));
            if (i == graphDto.getElementIds().size() - 1) {
                stringBuilder.append(" ) ");
            } else {
                stringBuilder.append(" OR ");
            }
        }
        stringBuilder.append(" AND h.new_status_id = ");
        stringBuilder.append(statusId);
        if (graphDto.getElementIds().isEmpty()) {
            stringBuilder.append(HistorySqlQuery.SQL_GRAPH_GROUP_BY_AND_ORDER_BY_FOR_ALL_PRODUCTS);
        } else {
            stringBuilder.append(HistorySqlQuery.SQL_GRAPH_GROUP_BY_AND_ORDER_BY);
        }
        return stringBuilder.toString();
    }

    private static final class HistoryWithDetailExtractor implements ResultSetExtractor<List<History>> {

        private ComplaintDao complaintDao;
        private OrderDao orderDao;
        private ProductDao productDao;

        HistoryWithDetailExtractor(ComplaintDao complaintDao, OrderDao orderDao, ProductDao productDao) {
            this.complaintDao = complaintDao;
            this.orderDao = orderDao;
            this.productDao = productDao;
        }

        @Override
        public List<History> extractData(ResultSet rs) throws SQLException, DataAccessException {
            ArrayList<History> allHistory = new ArrayList<>();
            while (rs.next()) {
                History history = new RealHistory();
                history.setId(rs.getLong(HistorySqlQuery.PARAM_HISTORY_ID));
                history.setDateChangeStatus(rs.getTimestamp(HistorySqlQuery.PARAM_HISTORY_DATE_CHANGE_STATUS).toLocalDateTime());
                history.setDescChangeStatus(rs.getString(HistorySqlQuery.PARAM_HISTORY_DESC_CHANGE_STATUS));

                long statusId = rs.getLong(HistorySqlQuery.PARAM_HISTORY_NEW_STATUS_ID);
                if (statusId > 0) {
                    history.setNewStatus(Status.getStatusByID(statusId));
                }

                long orderId = rs.getLong(HistorySqlQuery.PARAM_HISTORY_ORDER_ID);
                if (orderId != 0) {
                    Order order = new OrderProxy(orderDao);
                    order.setId(orderId);
                    history.setOrder(order);
                }

                long complaintId = rs.getLong(HistorySqlQuery.PARAM_HISTORY_COMPLAINT_ID);
                if (complaintId != 0) {
                    Complaint complaint = new ComplaintProxy(complaintDao);
                    complaint.setId(complaintId);
                    history.setComplaint(complaint);
                }

                long productId = rs.getLong(HistorySqlQuery.PARAM_HISTORY_PRODUCT_ID);
                if (productId != 0) {
                    Product product = new ProductProxy(productDao);
                    product.setId(productId);
                    history.setProduct(product);
                }

                allHistory.add(history);
            }
            return allHistory;
        }
    }

    private static final class GraphExtractor implements ResultSetExtractor<GraphDto> {

        private GraphDto graphDto;
        private LocalDate fromDate;
        private LocalDate toDate;

        private List<List<Long>> series = new ArrayList<>();
        private List<String> labels = new ArrayList<>();

        public GraphExtractor(GraphDto graphDto, LocalDate fromDate, LocalDate toDate) {
            this.graphDto = graphDto;
            this.fromDate = fromDate;
            this.toDate = toDate;

            long betweenFromTo = graphDto.getBetweenDates(fromDate, toDate) + 1;
            int lengthSeries = 1;
            if (!graphDto.getElementIds().isEmpty()) {
                lengthSeries = graphDto.getElementIds().size();
            }
            for (int i = 0; i < lengthSeries; i++) {
                series.add(new ArrayList<>(Collections.nCopies((int) betweenFromTo, 0L)));
            }
        }

        @Override
        public GraphDto extractData(ResultSet rs) throws SQLException, DataAccessException {
            int seriesIndex;

            while (rs.next()) {
                LocalDate dataChange = rs.getTimestamp(HistorySqlQuery.PARAM_GRAPH_DATE_CHANGE).toLocalDateTime().toLocalDate();
                long count = rs.getLong(HistorySqlQuery.PARAM_GRAPH_COUNT);

                checkDate(dataChange);

                if (graphDto.getElementIds().isEmpty()) {
                    seriesIndex = 0;
                } else {
                    long elementId = rs.getLong(HistorySqlQuery.PARAM_GRAPH_ELEMENT_ID);
                    seriesIndex = graphDto.getElementIds().indexOf(elementId);
                }
                series.get(seriesIndex).set(labels.size(), count);
            }
            if (!fromDate.equals(toDate)) {
                checkDate(toDate);
            }
            addLabels();

            graphDto.setLabels(labels);
            graphDto.setSeries(series);
            return graphDto;
        }

        private void checkDate(LocalDate dataChange) {
            if (dataChange.equals(fromDate)) {
                return;
            } else {
                addLabels();
            }
            checkDate(dataChange);
        }

        private void addLabels() {
            switch (graphDto.getDateType()) {
                case YEARS:
                    labels.add(fromDate.format(DateTimeFormatter.ofPattern("yyyy")));
                    fromDate = fromDate.plusYears(1);
                    break;
                case MONTHS:
                    labels.add(fromDate.format(DateTimeFormatter.ofPattern("MM")));
                    fromDate = fromDate.plusMonths(1);
                    break;
                case DAYS:
                    labels.add(fromDate.format(DateTimeFormatter.ofPattern("dd")));
                    fromDate = fromDate.plusDays(1);
                    break;
            }
        }
    }
}
