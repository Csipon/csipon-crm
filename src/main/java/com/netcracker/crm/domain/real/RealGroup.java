package com.netcracker.crm.domain.real;

import com.netcracker.crm.domain.model.Discount;
import com.netcracker.crm.domain.model.Group;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RealGroup implements Group {
    private Long id;
    private String name;
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