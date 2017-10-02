package com.csipon.crm.validation.field;

/**
 * Created by bpogo on 5/6/2017.
 */
public enum UserDtoField implements DtoField {
    FIRST_NAME("firstName", "First Name"),
    MIDDLE_NAME("middleName", "Middle Name"),
    LAST_NAME("lastName", "Last Name"),
    EMAIL("email", "Email"),
    PHONE("phone", "Phone"),
    USER_ROLE("userRole", "User Role"),
    ORGANIZATION_NAME("organizationName", "Organization"),
    ADDRESS_LATITUDE("addressLatitude", "Address"),
    ADDRESS_LONGITUDE("addressLongitude", "Address"),
    ADDRESS_DETAILS("addressDetails", "Address details");


    UserDtoField(String dtoName, String errorName) {
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