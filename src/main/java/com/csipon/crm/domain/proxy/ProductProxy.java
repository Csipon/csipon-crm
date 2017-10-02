package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductStatus;

/**
 * @author Karpunets
 * @since 14.05.2017
 */
public class ProductProxy implements Product {
    private long id;
    private Product product;
    private ProductDao productDao;

    public ProductProxy(ProductDao productDao) {
        this.productDao = productDao;
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
        return getProduct().getTitle();
    }

    @Override
    public void setTitle(String title) {
        getProduct().setTitle(title);
    }

    @Override
    public Double getDefaultPrice() {
        return getProduct().getDefaultPrice();
    }

    @Override
    public void setDefaultPrice(Double defaultPrice) {
        getProduct().setDefaultPrice(defaultPrice);
    }

    @Override
    public ProductStatus getStatus() {
        return getProduct().getStatus();
    }

    @Override
    public void setStatus(ProductStatus status) {
        getProduct().setStatus(status);
    }

    @Override
    public String getDescription() {
        return getProduct().getDescription();
    }

    @Override
    public void setDescription(String description) {
        getProduct().setDescription(description);
    }

    @Override
    public Discount getDiscount() {
        return getProduct().getDiscount();
    }

    @Override
    public void setDiscount(Discount discount) {
        getProduct().setDiscount(discount);
    }

    @Override
    public Group getGroup() {
        return getProduct().getGroup();
    }

    @Override
    public void setGroup(Group group) {
        getProduct().setGroup(group);
    }

    private Product getProduct() {
        if (product == null) {
            product = productDao.findById(id);
        }
        return product;
    }
}