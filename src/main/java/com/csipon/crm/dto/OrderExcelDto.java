package com.csipon.crm.dto;

/**
 * Created by Pasha on 23.05.2017.
 */
public class OrderExcelDto {
    private Long[] idCustomer;
    private String dateFrom;
    private String dateTo;
    private Integer orderByIndex;

    public Long[] getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long[] idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getOrderByIndex() {
        return orderByIndex;
    }

    public void setOrderByIndex(Integer orderByIndex) {
        this.orderByIndex = orderByIndex;
    }
}
