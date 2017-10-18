package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.model.Discount;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class RealGroup implements Group {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String name;
    @JoinColumn(name = "discount_id", table = "discount", referencedColumnName = "id")
    private Discount discount;

    public RealGroup() {
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

    @Override
    public Discount getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}