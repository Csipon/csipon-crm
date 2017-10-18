package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Organization;

import javax.persistence.*;

@Entity
@Table(name = "organization")
public class RealOrganization implements Organization {
    @Id
    @GeneratedValue
    private Long id;
    @Column
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