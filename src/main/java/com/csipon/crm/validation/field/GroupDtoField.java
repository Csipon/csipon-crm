package com.csipon.crm.validation.field;

/**
 * Created by Pasha on 06.05.2017.
 */
public enum GroupDtoField implements DtoField {

    NAME("name", "Name");

    private final String name;
    private final String errorName;

    GroupDtoField(String name, String errorName) {
        this.name = name;
        this.errorName = errorName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getErrorName() {
        return this.errorName;
    }
}
