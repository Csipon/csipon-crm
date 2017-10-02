package com.csipon.crm.domain.model;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public enum ProductStatus implements Status {
    PLANNED(12L, "PLANNED"),
    ACTUAL(13L, "ACTUAL"),
    OUTDATED(14L, "OUTDATED");

    private Long id;
    private String name;

    ProductStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
