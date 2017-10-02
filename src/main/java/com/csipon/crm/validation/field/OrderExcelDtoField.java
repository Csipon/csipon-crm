package com.csipon.crm.validation.field;

/**
 * Created by Pasha on 25.05.2017.
 */
public enum OrderExcelDtoField implements DtoField {
    ORDER_BY_INDEX("orderByIndex", "Order by index"),
    ID_CUSTOMERS("idCustomer", "Customers"),
    DATE_FROM("dateFrom", "Date from"),
    DATE_TO("dateTo", "Date to");

    private final String name;
    private final String errorName;

    OrderExcelDtoField(String name, String errorName) {
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
