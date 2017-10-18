package com.csipon.crm.domain.model;


public enum ComplaintStatus implements Status {
    OPEN(1L, "OPEN"),
    SOLVING(2L, "SOLVING"),
    CLOSED(3L, "CLOSED");

    private Long id;
    private String name;

    ComplaintStatus(Long id, String name) {
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
