package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.model.state.order.OrderState;
import com.csipon.crm.domain.proxy.ProductProxy;
import com.csipon.crm.domain.proxy.UserProxy;
import com.csipon.crm.domain.real.RealOrder;
import com.csipon.crm.domain.request.OrderRowRequest;
import com.csipon.crm.domain.request.RowRequest;
import com.csipon.crm.scheduler.OrderSchedulerSqlGenerator;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.csipon.crm.dao.impl.sql.OrderSqlQuery.*;

/**
 * @author Karpunets
 * @since 01.06.2017
 */
@Repository
public class OrderDaoImpl implements OrderDao {
    private static final Logger log = LoggerFactory.getLogger(OrderDaoImpl.class);

    private final UserDao userDao;
    private final ProductDao productDao;

    private SimpleJdbcInsert orderInsert;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private OrderWithDetailExtractor orderWithDetailExtractor;

    @Autowired
    public OrderDaoImpl(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.orderInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARAM_ORDER_TABLE)
                .usingGeneratedKeyColumns(PARAM_ORDER_ID);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.orderWithDetailExtractor = new OrderWithDetailExtractor(userDao, productDao);
    }

    @Override
    public Long create(Order order) {
        if (order.getId() != null) {
            return null;
        }
        Long customerId = order.getCustomer().getId();
        Long productId = order.getProduct().getId();
        Long csrId = getUserId(order.getCsr());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_DATE_FINISH, order.getDate())
                .addValue(PARAM_ORDER_PREFERRED_DATE, order.getPreferedDate())
                .addValue(PARAM_ORDER_STATUS, order.getStatus().getId())
                .addValue(PARAM_CUSTOMER_ID, customerId)
                .addValue(PARAM_PRODUCT_ID, productId)
                .addValue(PARAM_CSR_ID, csrId);

        long newId = orderInsert.executeAndReturnKey(params)
                .longValue();
        order.setId(newId);

        log.info("Order with id: " + newId + " is successfully created.");
        return newId;
    }

    private Long getUserId(User user) {
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    @Override
    public List<Order> findAllByPrefDateAndStatus(OrderSchedulerSqlGenerator generator, List<User> csrs,
                                                  LocalDateTime to, OrderStatus orderStatus) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_PREFERRED_DATE, to)
                .addValue(PARAM_ORDER_STATUS, orderStatus.getId());
        String sql = generator.generateSqlForOnlineCsr(SQL_FIND_ALL_ORDER_BY_DATE_LESS, PARAM_CSR_ID, csrs.size());
        for (int i = 0; i < csrs.size(); i++) {
            params.addValue(generator.getField() + i, csrs.get(i).getId());
        }
        return namedJdbcTemplate.query(sql, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findAllByStatus(OrderSchedulerSqlGenerator generator, List<User> csrs,
                                       OrderStatus orderStatus) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_STATUS, orderStatus.getId());
        String sql = generator.generateSqlForOnlineCsr(SQL_FIND_ALL_ORDER_BY_STATUS, PARAM_CSR_ID, csrs.size());
        for (int i = 0; i < csrs.size(); i++) {
            params.addValue(generator.getField() + i, csrs.get(i).getId());
        }
        return namedJdbcTemplate.query(sql, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findAllByCsrId(LocalDateTime to, OrderStatus orderStatus, Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_PREFERRED_DATE, to)
                .addValue(PARAM_ORDER_STATUS, orderStatus.getId())
                .addValue(PARAM_CSR_ID, id);
        return namedJdbcTemplate.query(SQL_FIND_ALL_ORDER_BY_CSR_AND_DATE, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findAllByCsrId(OrderStatus orderStatus, Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_STATUS, orderStatus.getId())
                .addValue(PARAM_CSR_ID, id);
        return namedJdbcTemplate.query(SQL_FIND_ALL_ORDER_BY_CSR, params, orderWithDetailExtractor);
    }

    @Override
    public Long update(Order order) {
        Long orderId = order.getId();
        if (orderId == null) {
            return null;
        }
        Long customerId = order.getCustomer().getId();
        Long productId = order.getProduct().getId();
        Long csrId = getUserId(order.getCsr());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_ID, orderId)
                .addValue(PARAM_ORDER_DATE_FINISH, order.getDate())
                .addValue(PARAM_ORDER_PREFERRED_DATE, order.getPreferedDate())
                .addValue(PARAM_ORDER_STATUS, order.getStatus().getId())
                .addValue(PARAM_CUSTOMER_ID, customerId)
                .addValue(PARAM_PRODUCT_ID, productId)
                .addValue(PARAM_CSR_ID, csrId);

        int updatedRows = namedJdbcTemplate.update(SQL_UPDATE_ORDER, params);

        if (updatedRows > 0) {
            log.info("Order with id: " + orderId + " is successfully updated.");
            return orderId;
        } else {
            log.error("Order was not updated.");
            return null;
        }
    }

    @Override
    public Long delete(Long id) {
        if (id != null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue(PARAM_ORDER_ID, id);
            long deletedRows = namedJdbcTemplate.update(SQL_DELETE_ORDER, params);
            if (deletedRows == 0) {
                log.error("Order has not been deleted");
                return null;
            } else {
                log.info("Order with id " + id + " was successfully deleted");
                return deletedRows;
            }
        }
        return null;
    }

    @Override
    public Long delete(Order order) {
        if (order != null) {
            return delete(order.getId());
        }
        return null;
    }

    @Override
    public Order findById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_ID, id);
        List<Order> allOrder = namedJdbcTemplate.query(SQL_FIND_ORDER_BY_ID, params, orderWithDetailExtractor);
        Order order = null;
        if (allOrder.size() != 0) {
            order = allOrder.get(0);
        }
        return order;
    }

    @Override
    public List<Order> findAllByDateFinish(LocalDate date) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_DATE_FINISH, date);
        return namedJdbcTemplate.query(SQL_FIND_ALL_ORDER_BY_DATE_FINISH, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findAllByPreferredDate(LocalDate date) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_PREFERRED_DATE, date);
        return namedJdbcTemplate.query(SQL_FIND_ALL_ORDER_BY_PREFERRED_DATE, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findAllByProductId(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_ID, id);
        return namedJdbcTemplate.query(SQL_FIND_ALL_ORDER_BY_PRODUCT_ID, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findAllByCustomerId(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_CUSTOMER_ID, id);
        return namedJdbcTemplate.query(SQL_FIND_ALL_ORDER_BY_CUSTOMER_ID, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findAllByCsrId(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_CSR_ID, id);
        return namedJdbcTemplate.query(SQL_FIND_ALL_ORDER_BY_CSR_ID, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findAllByCustomerIds(List<Long> id, LocalDate from, LocalDate to, int orderByIndex) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_CUSTOMER_ID, id)
                .addValue(PARAM_DATE_FROM, from)
                .addValue(PARAM_DATE_TO, to)
                .addValue(PARAM_ORDER_BY_INDEX , orderByIndex);
        String sql = SQL_FIND_ORDER_BY_CUSTOMER_IDS + " ORDER BY " + orderByIndex +" ASC;";
        return namedJdbcTemplate.query(sql, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findOrderRows(OrderRowRequest orderRowRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_ROW_STATUS, orderRowRequest.getStatusId())
                .addValue(PARAM_ORDER_ROW_PRODUCT_STATUS, orderRowRequest.getProductStatusId())
                .addValue(PARAM_CUSTOMER_ID, orderRowRequest.getCustomerId())
                .addValue(RowRequest.PARAM_ROW_LIMIT, orderRowRequest.getRowLimit())
                .addValue(RowRequest.PARAM_ROW_OFFSET, orderRowRequest.getRowOffset());

        String sql = orderRowRequest.getSql();

        if (orderRowRequest.getKeywordsArray() != null) {
            int i = 0;
            for (String keyword : orderRowRequest.getKeywordsArray()) {
                params.addValue(RowRequest.PARAM_KEYWORD + i++, "%" + keyword + "%");
            }
        }

        return namedJdbcTemplate.query(sql, params, orderWithDetailExtractor);
    }

    @Override
    public Long getOrderRowsCount(OrderRowRequest orderRowRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_ROW_STATUS, orderRowRequest.getStatusId())
                .addValue(PARAM_ORDER_ROW_PRODUCT_STATUS, orderRowRequest.getProductStatusId())
                .addValue(PARAM_CUSTOMER_ID, orderRowRequest.getCustomerId());

        String sql = orderRowRequest.getSqlCount();

        if (orderRowRequest.getKeywordsArray() != null) {
            int i = 0;
            for (String keyword : orderRowRequest.getKeywordsArray()) {
                params.addValue(RowRequest.PARAM_KEYWORD + i++, "%" + keyword + "%");
            }
        }

        return namedJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    @Override
    public Boolean hasCustomerProduct(Long productId, Long customerId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_ID, productId)
                .addValue(PARAM_CUSTOMER_ID, customerId);
        return namedJdbcTemplate.queryForObject(SQL_HAS_CUSTOMER_PRODUCT, params, Boolean.class);
    }

    @Override
    public Long checkOwnershipOfContactPerson(Long orderId, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_ID, orderId)
                .addValue(PARAM_CUSTOMER_ID, userId);
        return namedJdbcTemplate.queryForObject(SQL_CHECK_OWNERSHIP_OF_CONTACT_PERSON, params, Long.class);
    }

    @Override
    public Long checkOwnershipOfCustomer(Long orderId, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_ORDER_ID, orderId)
                .addValue(PARAM_CUSTOMER_ID, userId);
        return namedJdbcTemplate.queryForObject(SQL_CHECK_OWNERSHIP_OF_CUSTOMER, params, Long.class);
    }

    @Override
    public List<Order> findByIdOrTitleByCustomer(String pattern, Long customerId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PATTERN, "%" + pattern + "%")
                .addValue(PARAM_CUSTOMER_ID, customerId);
        return namedJdbcTemplate.query(SQL_FIND_ORDER_BY_ID_OR_PRODUCT_TITLE, params, orderWithDetailExtractor);
    }

    @Override
    public List<Order> findOrgOrdersByIdOrTitle(String pattern, Long customerId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PATTERN, "%" + pattern + "%")
                .addValue(PARAM_CUSTOMER_ID, customerId);
        return namedJdbcTemplate.query(SQL_FIND_ORG_ORDERS_BY_ID_OR_PRODUCT_TITLE, params, orderWithDetailExtractor);
    }

    private static final class OrderWithDetailExtractor implements ResultSetExtractor<List<Order>> {

        private UserDao userDao;
        private ProductDao productDao;

        OrderWithDetailExtractor(UserDao userDao, ProductDao productDao) {
            this.userDao = userDao;
            this.productDao = productDao;
        }

        @Override
        public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
            ArrayList<Order> allOrder = new ArrayList<>();
            while (rs.next()) {
                Order order = new RealOrder();
                order.setId(rs.getLong(PARAM_ORDER_ID));

                Timestamp dateFinish = rs.getTimestamp(PARAM_ORDER_DATE_FINISH);
                if (dateFinish != null) {
                    order.setDate(dateFinish.toLocalDateTime());
                }

                Timestamp datePreferred = rs.getTimestamp(PARAM_ORDER_PREFERRED_DATE);
                if (datePreferred != null) {
                    order.setPreferedDate(datePreferred.toLocalDateTime());
                }

                long statusId = rs.getLong(PARAM_ORDER_STATUS);
                Status status = Status.getStatusByID(statusId);
                if (status instanceof OrderStatus) {
                    order.setStatus((OrderStatus) status);
                    OrderState.setStateForOrder((OrderStatus) status, order);
                }

                long customerId = rs.getLong(PARAM_CUSTOMER_ID);
                if (customerId != 0) {
                    User customer = new UserProxy(userDao);
                    customer.setId(customerId);
                    order.setCustomer(customer);
                }

                long productId = rs.getLong(PARAM_PRODUCT_ID);
                if (productId != 0) {
                    Product product = new ProductProxy(productDao);
                    product.setId(productId);
                    order.setProduct(product);
                }

                long csrId = rs.getLong(PARAM_CSR_ID);
                if (csrId != 0) {
                    User csr = new UserProxy(userDao);
                    csr.setId(csrId);
                    order.setCsr(csr);
                }

                allOrder.add(order);
            }
            return allOrder;
        }
    }
}
