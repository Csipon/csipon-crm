package com.csipon.crm.validation.field;

/**
 * Created by Pasha on 06.05.2017.
 */
public enum ProductDtoField implements DtoField {

    TITLE("title", "Title"),
    DEFAULT_PRICE("defaultPrice", "Price"),
    DESCRIPTION("description", "Description");

    ProductDtoField(String dtoName, String errorName) {
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
