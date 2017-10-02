package com.csipon.crm.domain.real;


import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.model.ProductStatus;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Product;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RealProduct implements Product {
    private Long id;
    private String title;
    private Double defaultPrice;
    private ProductStatus status;
    private String description;
    private Discount discount;
    private Group group;

    public RealProduct() {
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
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Double getDefaultPrice() {
        return defaultPrice;
    }

    @Override
    public void setDefaultPrice(Double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    @Override
    public ProductStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Discount getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealProduct product = (RealProduct) o;

        return id != null ? id.equals(product.id) : product.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (defaultPrice != null ? defaultPrice.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }
}