package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.dao.GroupDao;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.domain.model.*;
import com.netcracker.crm.domain.model.*;
import com.csipon.crm.domain.proxy.DiscountProxy;
import com.csipon.crm.domain.proxy.GroupProxy;
import com.csipon.crm.domain.real.RealProduct;
import com.csipon.crm.domain.request.ProductRowRequest;
import com.csipon.crm.domain.request.RowRequest;
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

import static com.csipon.crm.dao.impl.sql.ProductSqlQuery.*;

/**
 * @author Karpunets
 * @since 29.04.2017
 */
@Repository
public class ProductDaoImpl implements ProductDao {
    private static final Logger log = LoggerFactory.getLogger(ProductDaoImpl.class);

    private DiscountDao discountDao;
    private GroupDao groupDao;

    private SimpleJdbcInsert productInsert;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private ProductWithDetailExtractor productWithDetailExtractor;

    @Autowired
    public ProductDaoImpl(DiscountDao discountDao, GroupDao groupDao) {
        this.discountDao = discountDao;
        this.groupDao = groupDao;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.productInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARAM_PRODUCT_TABLE)
                .usingGeneratedKeyColumns(PARAM_PRODUCT_ID);
        productWithDetailExtractor = new ProductWithDetailExtractor(discountDao, groupDao);
    }

    @Override
    public Long create(Product product) {
        if (product.getId() != null) {
            return null;
        }

        Long discountId = getDiscountId(product.getDiscount());
        Long groupId = getGroupId(product.getGroup());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_TITLE, product.getTitle())
                .addValue(PARAM_PRODUCT_DEFAULT_PRICE, product.getDefaultPrice())
                .addValue(PARAM_PRODUCT_STATUS_ID, product.getStatus().getId())
                .addValue(PARAM_PRODUCT_DESCRIPTION, product.getDescription())
                .addValue(PARAM_PRODUCT_DISCOUNT_ID, discountId)
                .addValue(PARAM_PRODUCT_GROUP_ID, groupId);

        long newId = productInsert.executeAndReturnKey(params)
                .longValue();

        product.setId(newId);

        log.info("Product with id: " + newId + " is successfully created.");
        return newId;
    }

    @Override
    public Long update(Product product) {
        Long productId = product.getId();
        if (productId == null) {
            return null;
        }
        Long discountId = getDiscountId(product.getDiscount());
        Long groupId = getGroupId(product.getGroup());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_ID, productId)
                .addValue(PARAM_PRODUCT_TITLE, product.getTitle())
                .addValue(PARAM_PRODUCT_DEFAULT_PRICE, product.getDefaultPrice())
                .addValue(PARAM_PRODUCT_STATUS_ID, product.getStatus().getId())
                .addValue(PARAM_PRODUCT_DESCRIPTION, product.getDescription())
                .addValue(PARAM_PRODUCT_DISCOUNT_ID, discountId)
                .addValue(PARAM_PRODUCT_GROUP_ID, groupId);

        int affectedRows = namedJdbcTemplate.update(SQL_UPDATE_PRODUCT, params);

        if (affectedRows > 0) {
            log.info("Product with id: " + productId + " is successfully updated.");
            return productId;
        } else {
            log.error("Product was not updated.");
            return -1L;
        }
    }

    @Override
    public Long delete(Long id) {
        if (id != null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue(PARAM_PRODUCT_ID, id);
            long deletedRows = namedJdbcTemplate.update(SQL_DELETE_PRODUCT, params);
            if (deletedRows == 0) {
                log.error("Product has not been deleted");
                return null;
            } else {
                log.info("Product with id " + id + " was successfully deleted");
                return deletedRows;
            }
        }
        return null;
    }

    @Override
    public Long delete(Product product) {
        if (product != null) {
            return delete(product.getId());
        }
        return null;
    }

    @Override
    public Product findById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_ID, id);
        return findBySqlParameterSource(params, SQL_FIND_PRODUCT_BY_ID);
    }

    @Override
    public Product findByTitle(String title) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_TITLE, title);
        return findBySqlParameterSource(params, SQL_FIND_PRODUCT_BY_TITLE);
    }


    private Product findBySqlParameterSource(SqlParameterSource params, String sql){
        List<Product> allProduct = namedJdbcTemplate.query(sql, params, productWithDetailExtractor);
        Product product = null;
        if (allProduct.size() != 0) {
            product = allProduct.get(0);
        }
        return product;
    }

    @Override
    public List<Product> findAllByGroupId(Long groupId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_GROUP_ID, groupId);
        return namedJdbcTemplate.query(SQL_FIND_ALL_PRODUCT_BY_GROUP_ID, params, productWithDetailExtractor);
    }

    @Override
    public List<Product> findAllByPattern(String pattern) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PATTERN, "%" + pattern + "%");
        return namedJdbcTemplate.query(SQL_FIND_ALL_PRODUCT_BY_ID_OR_TITLE, params, productWithDetailExtractor);
    }

    @Override
    public List<Product> findWithoutGroupByPattern(String pattern) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PATTERN, "%" + pattern + "%");
        return namedJdbcTemplate.query(SQL_FIND_PRODUCT_WITHOUT_GROUP_BY_ID_OR_TITLE, params, productWithDetailExtractor);
    }

    @Override
    public List<Product> findActualByPatternAndCustomerId(String pattern, Long customerId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PATTERN, "%" + pattern + "%")
                .addValue(PARAM_PRODUCT_CUSTOMER_ID, customerId);
        return namedJdbcTemplate.query(SQL_FIND_ACTUAL_PRODUCT_BY_ID_OR_TITLE_AND_CUSTOMER_ID, params, productWithDetailExtractor);
    }

    @Override
    public List<Product> findByPatternAndCustomerIdAndRegionId(String pattern, Long customerId, Long regionId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PATTERN, "%" + pattern + "%")
                .addValue(PARAM_PRODUCT_CUSTOMER_ID, customerId)
                .addValue(PARAM_PRODUCT_REGION_ID, regionId);
        return namedJdbcTemplate.query(SQL_FIND_POSSIBLE_PRODUCT_BY_ID_OR_TITLE_AND_CUSTOMER_ID, params, productWithDetailExtractor);
    }

    @Override
    public Long getProductRowsCount(ProductRowRequest orderRowRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_ROW_STATUS, orderRowRequest.getStatusId())
                .addValue(PARAM_PRODUCT_ROW_PRODUCT_DISCOUNT_ACTIVE, orderRowRequest.getDiscountActive())
                .addValue(PARAM_PRODUCT_ROW_GROUP_DISCOUNT_ACTIVE, orderRowRequest.getGroupDiscountActive())
                .addValue(PARAM_PRODUCT_CUSTOMER_ID, orderRowRequest.getCustomerId());

        if (orderRowRequest.getAddress() != null) {
            params.addValue(PARAM_PRODUCT_REGION_ID, orderRowRequest.getAddress().getRegion().getId());
        }

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
    public List<Product> findProductRows(ProductRowRequest orderRowRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_ROW_STATUS, orderRowRequest.getStatusId())
                .addValue(PARAM_PRODUCT_ROW_PRODUCT_DISCOUNT_ACTIVE, orderRowRequest.getDiscountActive())
                .addValue(PARAM_PRODUCT_ROW_GROUP_DISCOUNT_ACTIVE, orderRowRequest.getGroupDiscountActive())
                .addValue(RowRequest.PARAM_ROW_LIMIT, orderRowRequest.getRowLimit())
                .addValue(RowRequest.PARAM_ROW_OFFSET, orderRowRequest.getRowOffset())
                .addValue(PARAM_PRODUCT_CUSTOMER_ID, orderRowRequest.getCustomerId());

        if (orderRowRequest.getAddress() != null) {
            params.addValue(PARAM_PRODUCT_REGION_ID, orderRowRequest.getAddress().getRegion().getId());
        }

        String sql = orderRowRequest.getSql();

        if (orderRowRequest.getKeywordsArray() != null) {
            int i = 0;
            for (String keyword : orderRowRequest.getKeywordsArray()) {
                params.addValue(RowRequest.PARAM_KEYWORD + i++, "%" + keyword + "%");
            }
        }

        return namedJdbcTemplate.query(sql, params, productWithDetailExtractor);
    }

    @Override
    public Boolean hasCustomerAccessToProduct(Long productId, Long customerId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_ID, productId)
                .addValue(PARAM_PRODUCT_CUSTOMER_ID, customerId);
        return namedJdbcTemplate.queryForObject(SQL_HAS_CUSTOMER_ACCESS_TO_PRODUCT, params, Boolean.class);

    }

    @Override
    public boolean hasSameStatus(Set<Long> productIDs) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_IDS, productIDs);

        boolean isSame = namedJdbcTemplate.queryForObject(SQL_GROUP_PRODUCTS_BY_STATUS, params, Integer.class) == 1;
        return isSame;
    }

    @Override
    public List<Product> findProductsByDiscountId(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_DISCOUNT_ID, id);
        return namedJdbcTemplate.query(SQL_FIND_PRODUCTS_BY_DISCOUNT_ID, params, productWithDetailExtractor);
    }


    @Override
    public List<Product> findProductsByDiscountIdAndCustomerId(Long discountId, Long customerId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_DISCOUNT_ID, discountId)
                .addValue(PARAM_PRODUCT_CUSTOMER_ID, customerId);
        return namedJdbcTemplate.query(SQL_FIND_PRODUCTS_BY_DISCOUNT_ID_AND_CUSTOMER_ID, params, productWithDetailExtractor);
    }

    @Override
    public boolean bulkUpdate(Set<Long> productIDs, Product product) {
        Long groupId = null;
        Long discountId = null;
        if (product.getGroup() != null) groupId = product.getGroup().getId();
        if (product.getDiscount() != null) discountId = product.getDiscount().getId();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_IDS, productIDs)
                .addValue(PARAM_PRODUCT_GROUP_ID, groupId)
                .addValue(PARAM_PRODUCT_DISCOUNT_ID, discountId)
                .addValue(PARAM_PRODUCT_DEFAULT_PRICE, product.getDefaultPrice())
                .addValue(PARAM_PRODUCT_DESCRIPTION, product.getDescription());

        return namedJdbcTemplate.queryForObject(SQL_PRODUCT_BULK_UPDATE, params, Integer.class) == productIDs.size();
    }

    private Long getDiscountId(Discount discount) {
        if (discount != null) {
            return discount.getId();
        }
        return null;
    }

    private Long getGroupId(Group group) {
        if (group != null) {
            Long groupId = group.getId();
            if (groupId != null) {
                return groupId;
            }
        }
        return null;
    }

    private static final class ProductWithDetailExtractor implements ResultSetExtractor<List<Product>> {

        private DiscountDao discountDao;
        private GroupDao groupDao;

        public ProductWithDetailExtractor(DiscountDao discountDao, GroupDao groupDao) {
            this.discountDao = discountDao;
            this.groupDao = groupDao;
        }

        @Override
        public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
            ArrayList<Product> allProduct = new ArrayList<>();
            while (rs.next()) {
                Product product = new RealProduct();
                product.setId(rs.getLong(PARAM_PRODUCT_ID));
                product.setTitle(rs.getString(PARAM_PRODUCT_TITLE));
                product.setDefaultPrice(rs.getDouble(PARAM_PRODUCT_DEFAULT_PRICE));
                product.setDescription(rs.getString(PARAM_PRODUCT_DESCRIPTION));

                Status status = Status.getStatusByID(rs.getLong(PARAM_PRODUCT_STATUS_ID));
                if (status instanceof ProductStatus) {
                    product.setStatus((ProductStatus) status);
                }

                long discountId = rs.getLong(PARAM_PRODUCT_DISCOUNT_ID);
                if (discountId != 0) {
                    Discount discount = new DiscountProxy(discountDao);
                    discount.setId(discountId);
                    product.setDiscount(discount);
                }

                long groupId = rs.getLong(PARAM_PRODUCT_GROUP_ID);
                if (groupId != 0) {
                    Group group = new GroupProxy(groupDao);
                    group.setId(groupId);
                    product.setGroup(group);
                }

                allProduct.add(product);
            }
            return allProduct;
        }
    }
}
