package com.csipon.crm.domain.model;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */

public enum UserRole {
    ROLE_ADMIN(1L, "ROLE_ADMIN", "Administrator"),
    ROLE_CUSTOMER(2L, "ROLE_CUSTOMER", "Customer"),
    ROLE_CSR(3L, "ROLE_CSR", "CSR"),
    ROLE_PMG(4L, "ROLE_PMG", "PMG");

    private final Long id;
    private final String name;
    private final String formattedName;

    UserRole(Long id, String name, String formattedName) {
        this.id = id;
        this.name = name;
        this.formattedName = formattedName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormattedName() {
        return formattedName;
    }
}
