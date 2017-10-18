package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Region;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;

@Entity
@Table(name = "region")
public class RealRegion implements Region {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    @JoinColumn(name = "discount_id", table = "discount", referencedColumnName = "id")
    private Discount discount;

    public RealRegion() {
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}