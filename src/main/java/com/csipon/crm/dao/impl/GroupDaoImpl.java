package com.csipon.crm.dao.impl;

import com.csipon.crm.domain.model.Group;
import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.dao.GroupDao;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.proxy.DiscountProxy;
import com.csipon.crm.domain.real.RealGroup;
import com.csipon.crm.domain.request.GroupRowRequest;
import com.csipon.crm.domain.request.RowRequest;
import com.csipon.crm.dto.GroupTableDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.csipon.crm.dao.impl.sql.GroupSqlQuery.*;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */
@Repository
public class GroupDaoImpl implements GroupDao {
    private static final Logger log = LoggerFactory.getLogger(GroupDaoImpl.class);

    private DiscountDao discountDao;

    private SimpleJdbcInsert insert;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private GroupExtractor groupExtractor;
    private GroupTableDtoExtractor groupTableExtractor;

    @Autowired
    public GroupDaoImpl(DiscountDao discountDao) {
        this.discountDao = discountDao;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARAM_GROUP_TABLE)
                .usingGeneratedKeyColumns(PARAM_GROUP_ID);
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        groupExtractor = new GroupExtractor(discountDao);
        groupTableExtractor = new GroupTableDtoExtractor();
    }

    @Override
    public Long create(Group group) {
        if (group.getId() != null) {
            return null;
        }
        Long discountId = getDiscountId(group.getDiscount());
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_NAME, group.getName())
                .addValue(PARAM_GROUP_DISCOUNT_ID, discountId);
        long id = insert.executeAndReturnKey(params).longValue();
        group.setId(id);
        log.info("Group with id: " + id + " is successfully created.");
        return id;
    }

    @Override
    public Long update(Group group) {
        Long groupId = group.getId();
        if (groupId == null) {
            return null;
        }
        Long discountId = getDiscountId(group.getDiscount());
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_ID, groupId)
                .addValue(PARAM_GROUP_NAME, group.getName())
                .addValue(PARAM_GROUP_DISCOUNT_ID, discountId);
        long affectedRows = namedJdbcTemplate.update(SQL_UPDATE_GROUP, params);
        if (affectedRows == 0) {
            log.error("Group has not been updated");
            return null;
        } else {
            log.info("Group with id " + groupId + " was successfully updated");
            return groupId;
        }
    }

    @Override
    public Long delete(Long id) {
        if (id != null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue(PARAM_GROUP_ID, id);
            long deletedRows = namedJdbcTemplate.update(SQL_DELETE_GROUP, params);
            if (deletedRows == 0) {
                log.error("Group has not been deleted");
                return null;
            } else {
                log.info("Group with id " + id + " was successfully deleted");
                return deletedRows;
            }
        }
        return null;
    }

    @Override
    public Long delete(Group group) {
        if (group != null) {
            return delete(group.getId());
        }
        return null;
    }

    @Override
    public Group findById(Long id) {
        log.debug("Start finding group by id");
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_ID, id);
        Group group = null;
        List<Group> groups = namedJdbcTemplate.query(SQL_FIND_GROUP_BY_ID, params, groupExtractor);
        if (groups.size() != 0) {
            group = groups.get(0);
        }
        log.debug("End finding group by id");
        return group;
    }

    @Override
    public List<Group> findByName(String name) {
        log.debug("Start finding groups by name");
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_NAME, "%" + name + "%");
        List<Group> list = namedJdbcTemplate.query(SQL_FIND_GROUP_BY_NAME, params, groupExtractor);
        log.debug("End finding groups by name");
        return list;
    }

    @Override
    public Long getCount() {
        return namedJdbcTemplate.getJdbcOperations().queryForObject(SQL_GET_GROUP_COUNT, Long.class);
    }

    @Override
    public Long getCount(GroupRowRequest request) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_ROW_DISCOUNT_ACTIVE, request.getDiscountActive());
        String query = request.getSqlCount();
        query = prepareQuery(query, params, request);
        return namedJdbcTemplate.queryForObject(query, params, Long.class);
    }

    @Override
    public List<GroupTableDto> getPartRows(GroupRowRequest request) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_ROW_DISCOUNT_ACTIVE, request.getDiscountActive())
                .addValue(RowRequest.PARAM_ROW_LIMIT, request.getRowLimit())
                .addValue(RowRequest.PARAM_ROW_OFFSET, request.getRowOffset());
        String query = request.getSql();
        query = prepareQuery(query, params, request);
        return namedJdbcTemplate.query(query, params, groupTableExtractor);
    }

    private String prepareQuery(String query, MapSqlParameterSource params, GroupRowRequest request) {
        String resultQuery = query;
        if (request.getKeywordsArray() != null) {
            int i = 0;
            for (String keyword : request.getKeywordsArray()) {
                resultQuery = resultQuery.replace("OR cast(products as text) ILIKE :keyword" + i, " ");
                params.addValue(RowRequest.PARAM_KEYWORD + i++, "%" + keyword + "%");
            }
        }
        return resultQuery;
    }

    @Override
    public List<Group> findByIdOrTitle(String pattern) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PATTERN, "%" + pattern + "%");
        return namedJdbcTemplate.query(SQL_FIND_GROUP_BY_ID_OR_TITLE, params, groupExtractor);
    }

    @Override
    public List<Group> findByDiscountId(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_DISCOUNT_ID, id);
        return namedJdbcTemplate.query(SQL_FIND_GROUP_BY_DISCOUNT_ID, params, groupExtractor);
    }

    @Override
    public List<Group> findByDiscountIdAndCustomerId(Long discountId, Long customerId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_DISCOUNT_ID, discountId)
                .addValue(PARAM_GROUP_CUSTOMER_ID, customerId);
        return namedJdbcTemplate.query(SQL_FIND_GROUP_BY_DISCOUNT_ID_AND_CUSTOMER_ID, params, groupExtractor);
    }

    @Override
    public boolean bulkUpdate(Set<Long> groupIDs, RealGroup group) {
        Long discountId = null;
        if (group.getDiscount() != null) discountId = group.getDiscount().getId();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_GROUP_IDS, groupIDs)
                .addValue(PARAM_GROUP_DISCOUNT_ID, discountId);
        return namedJdbcTemplate.queryForObject(SQL_GROUP_BULK_UPDATE, params, Integer.class) == groupIDs.size();
    }

    private Long getDiscountId(Discount discount) {
        if (discount != null) {
            return discount.getId();
        }
        return null;
    }

    static final class GroupExtractor implements ResultSetExtractor<List<Group>> {

        private DiscountDao discountDao;

        GroupExtractor(DiscountDao discountDao) {
            this.discountDao = discountDao;
        }

        @Override
        public List<Group> extractData(ResultSet rs) throws SQLException, DataAccessException {
            log.debug("Start extracting data");
            List<Group> groups = new ArrayList<>();
            while (rs.next()) {
                Group group = new RealGroup();
                group.setId(rs.getLong(PARAM_GROUP_ID));
                group.setName(rs.getString(PARAM_GROUP_NAME));

                long discountId = rs.getLong(PARAM_GROUP_DISCOUNT_ID);
                if (discountId != 0) {
                    Discount discount = new DiscountProxy(discountDao);
                    discount.setId(discountId);
                    group.setDiscount(discount);
                }

                groups.add(group);
            }
            log.debug("End extracting data");
            return groups;
        }
    }

    static final class GroupTableDtoExtractor implements ResultSetExtractor<List<GroupTableDto>> {
        @Override
        public List<GroupTableDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            log.debug("Start extracting data");
            List<GroupTableDto> groups = new ArrayList<>();
            while (rs.next()) {
                GroupTableDto group = new GroupTableDto();
                group.setId(rs.getLong(PARAM_GROUP_ID));
                group.setName(rs.getString(PARAM_GROUP_NAME));
                group.setDiscountName(rs.getString(PARAM_GROUP_ROW_DISCOUNT_TITLE));
                if (group.getDiscountName() != null) {
                    group.setDiscountValue(rs.getDouble(PARAM_GROUP_ROW_DISCOUNT_VALUE));
                    group.setDiscountActive(rs.getBoolean(PARAM_GROUP_ROW_DISCOUNT_ACTIVE));
                }
                group.setNumberProducts(rs.getLong(PARAM_GROUP_ROW_PRODUCT_COUNT));
                groups.add(group);
            }
            log.debug("End extracting data");
            return groups;
        }
    }
}
