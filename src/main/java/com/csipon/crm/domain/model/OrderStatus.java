package com.csipon.crm.domain.model;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public enum OrderStatus implements Status {
    NEW(4L, "NEW"),
    PROCESSING(5L, "PROCESSING"),
    ACTIVE(6L, "ACTIVE"),
    DISABLED(7L, "DISABLED"),
    PAUSED(8L, "PAUSED"),
    REQUEST_TO_RESUME(9L, "REQUEST_TO_RESUME"),
    REQUEST_TO_PAUSE(10L, "REQUEST_TO_PAUSE"),
    REQUEST_TO_DISABLE(11L, "REQUEST_TO_DISABLE");

    private Long id;
    private String name;

    OrderStatus(Long id, String name) {
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
