package com.csipon.crm.dao;

import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.request.ProductRowRequest;

import java.util.List;
import java.util.Set;

/**
 * @author YARUS
 */
public interface ProductDao extends CrudDao<Product>, BulkDao<Product> {
    Product findByTitle(String title);

    List<Product> findAllByGroupId(Long id);

    List<Product> findAllByPattern(String pattern);
    List<Product> findWithoutGroupByPattern(String pattern);
    List<Product> findActualByPatternAndCustomerId(String pattern, Long customerId);
    List<Product> findByPatternAndCustomerIdAndRegionId(String pattern, Long customerId, Long regionId);

    Long getProductRowsCount(ProductRowRequest orderRowRequest);
    List<Product> findProductRows(ProductRowRequest orderRowRequest);

    Boolean hasCustomerAccessToProduct(Long productId, Long customerId);

    boolean hasSameStatus(Set<Long> productIDs);

    List<Product> findProductsByDiscountId(Long id);

    List<Product> findProductsByDiscountIdAndCustomerId(Long discountId, Long customerId);
}
