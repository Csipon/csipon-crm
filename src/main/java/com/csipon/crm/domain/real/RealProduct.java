package com.csipon.crm.domain.real;


import com.csipon.crm.domain.converter.ProductStatusConverter;
import com.csipon.crm.domain.model.*;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class RealProduct implements Product {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String title;
    @Column(name = "default_price")
    private Double defaultPrice;
    @Convert(converter = ProductStatusConverter.class)
    @Column(name = "status_id")
    private ProductStatus status;
    @Column
    private String description;
    @JoinColumn(name = "discount_id", table = "discount", referencedColumnName = "id")
    private Discount discount;
    @JoinColumn(name = "group_id", table = "groups", referencedColumnName = "id")
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