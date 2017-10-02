package com.csipon.crm.dao;

import com.csipon.crm.domain.real.RealDiscount;
import com.csipon.crm.domain.request.DiscountRowRequest;
import com.csipon.crm.domain.model.Discount;

import java.util.List;
import java.util.Set;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */
public interface DiscountDao extends CrudDao<Discount> {
    List<Discount> findByTitle(String title);

    Long getCount();

    Long getDiscountRowsCount(DiscountRowRequest rowRequest);

    List<Discount> findDiscounts(DiscountRowRequest rowRequest);

    List<Discount> findByIdOrTitle(String pattern);

    boolean bulkUpdate(Set<Long> discountIDs, RealDiscount discountTemplate);
}
