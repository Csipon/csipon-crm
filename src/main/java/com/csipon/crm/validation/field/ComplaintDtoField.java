package com.csipon.crm.validation.field;

/**
 * Created by Pasha on 06.05.2017.
 */
public enum ComplaintDtoField implements DtoField{
    TITLE("title", "Title"),
    MESSAGE("message", "Message"),
    ORDER_ID("orderId", "Order");

    ComplaintDtoField(String dtoName, String errorName) {
        this.name = dtoName;
        this.errorName = errorName;
    }

    private final String name;
    private final String errorName;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getErrorName() {
        return this.errorName;
    }
}
