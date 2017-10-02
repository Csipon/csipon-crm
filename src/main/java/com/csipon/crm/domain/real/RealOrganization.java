package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Organization;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RealOrganization implements Organization {
    private Long id;
    private String name;

    public RealOrganization() {
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