package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dao.ProductParamDao;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductParam;
import com.csipon.crm.domain.proxy.ProductProxy;
import com.csipon.crm.domain.real.RealProductParam;
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

import static com.csipon.crm.dao.impl.sql.DiscountSqlQuery.PARAM_PATTERN;
import static com.csipon.crm.dao.impl.sql.ProductParamSqlQuery.*;

@Repository
public class ProductParamDaoImpl implements ProductParamDao {

    private static final Logger log = LoggerFactory.getLogger(ProductParamDaoImpl.class);

    private ProductDao productDao;

    private SimpleJdbcInsert productParamInsert;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private ProductParamWithDetailExtractor productParamWithDetailExtractor;

    @Autowired
    public ProductParamDaoImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.productParamInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARAM_PRODUCT_PARAM_TABLE)
                .usingGeneratedKeyColumns(PARAM_PRODUCT_PARAM_ID);
        productParamWithDetailExtractor = new ProductParamWithDetailExtractor(productDao);
    }

    @Override
    public Long create(ProductParam productParam) {
        if (productParam.getId() != null) {
            return null;
        }

        Long productId = productParam.getProduct().getId();

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_PARAM_NAME, productParam.getParamName())
                .addValue(PARAM_PRODUCT_PARAM_VALUE, productParam.getValue())
                .addValue(PARAM_PRODUCT_PARAM_PRODUCT_ID, productId);

        long newId = productParamInsert.executeAndReturnKey(params).longValue();

        productParam.setId(newId);

        log.info("Parameter with id: " + newId + " is successfully created.");
        return newId;
    }


    @Override
    public Long update(ProductParam productParam) {
        Long productParamId = productParam.getId();
        if (productParamId == null) {
            return null;
        }
        Long productId = productParam.getProduct().getId();

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_PARAM_ID, productParamId)
                .addValue(PARAM_PRODUCT_PARAM_NAME, productParam.getParamName())
                .addValue(PARAM_PRODUCT_PARAM_VALUE, productParam.getValue())
                .addValue(PARAM_PRODUCT_PARAM_PRODUCT_ID, productId);

        int affectedRows = namedJdbcTemplate.update(SQL_UPDATE_PRODUCT_PARAM, params);

        if (affectedRows > 0) {
            log.info("Parameter with id: " + productParamId + " is successfully updated.");
            return productParamId;
        } else {
            log.error("Parameter was not updated.");
            return -1L;
        }
    }

    @Override
    public Long delete(Long id) {
        if (id != null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue(PARAM_PRODUCT_PARAM_ID, id);
            long deletedRows = namedJdbcTemplate.update(SQL_DELETE_PRODUCT_PARAM, params);
            if (deletedRows == 0) {
                log.error("Parameter has not been deleted");
                return null;
            } else {
                log.info("Parameter with id " + id + " was successfully deleted");
                return deletedRows;
            }
        }
        return null;
    }

    @Override
    public Long delete(ProductParam productParam) {
        if (productParam != null) {
            return delete(productParam.getId());
        }
        return null;
    }

    @Override
    public ProductParam findById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_PARAM_ID, id);

        List<ProductParam> allProductParams = namedJdbcTemplate.query(SQL_FIND_PRODUCT_PARAM_BY_ID, params, productParamWithDetailExtractor);
        ProductParam productParam = null;
        if (allProductParams.size() != 0) {
            productParam = allProductParams.get(0);
        }
        return productParam;
    }

    @Override
    public List<ProductParam> findAllByParamName(String paramName) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_PARAM_NAME, paramName);

        return namedJdbcTemplate.query(SQL_FIND_PRODUCT_PARAM_BY_NAME, params, productParamWithDetailExtractor);
    }

    @Override
    public List<ProductParam> findByIdOrTitle(String pattern) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PATTERN, "%" + pattern + "%");
        return namedJdbcTemplate.query(SQL_FIND_PARAM_BY_ID_OR_NAME, params, productParamWithDetailExtractor);
    }

    @Override
    public List<ProductParam> findAllByProductId(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PRODUCT_PARAM_PRODUCT_ID, id);
        return namedJdbcTemplate.query(SQL_FIND_PRODUCT_PARAMS_BY_PRODUCT_ID, params, productParamWithDetailExtractor);
    }

    private static final class ProductParamWithDetailExtractor implements ResultSetExtractor<List<ProductParam>> {

        private ProductDao productDao;

        public ProductParamWithDetailExtractor(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        public List<ProductParam> extractData(ResultSet rs) throws SQLException, DataAccessException {
            ArrayList<ProductParam> allProductParams = new ArrayList<>();
            while (rs.next()) {
                ProductParam productParam = new RealProductParam();
                productParam.setId(rs.getLong(PARAM_PRODUCT_PARAM_ID));
                productParam.setParamName(rs.getString(PARAM_PRODUCT_PARAM_NAME));
                productParam.setValue(rs.getString(PARAM_PRODUCT_PARAM_VALUE));

                long productId = rs.getLong(PARAM_PRODUCT_PARAM_PRODUCT_ID);
                if (productId != 0) {
                    Product product = new ProductProxy(productDao);
                    product.setId(productId);
                    productParam.setProduct(product);
                }

                allProductParams.add(productParam);
            }
            return allProductParams;
        }
    }
}
