package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.domain.model.Discount;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class DiscountProxy implements Discount {
    private long id;
    private Discount discount;
    private DiscountDao discountDao;

    public DiscountProxy(DiscountDao discountDao) {
        this.discountDao = discountDao;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return getDiscount().getTitle();
    }

    @Override
    public void setTitle(String title) {
        getDiscount().setTitle(title);
    }

    @Override
    public Double getPercentage() {
        return getDiscount().getPercentage();
    }

    @Override
    public void setPercentage(Double percentage) {
        getDiscount().setPercentage(percentage);
    }

    @Override
    public String getDescription() {
        return getDiscount().getDescription();
    }

    @Override
    public void setDescription(String description) {
        getDiscount().setDescription(description);
    }

    @Override
    public Boolean isActive() {
        return getDiscount().isActive();
    }

    @Override
    public void setActive(Boolean active) {
        getDiscount().setActive(active);
    }

    private Discount getDiscount() {
        if (discount == null) {
            discount = discountDao.findById(id);
        }
        return discount;
    }
}