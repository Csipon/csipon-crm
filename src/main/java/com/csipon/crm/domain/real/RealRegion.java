package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Region;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RealRegion implements Region {
    private Long id;
    private String name;
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
}