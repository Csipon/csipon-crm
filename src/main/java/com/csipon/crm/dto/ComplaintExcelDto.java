package com.csipon.crm.dto;

/**
 * Created by Pasha on 26.05.2017.
 */
public class ComplaintExcelDto {
    private Long[] idProduct;
    private String dateFrom;
    private String dateTo;
    private Integer orderByIndex;

    public Long[] getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long[] idProduct) {
        this.idProduct = idProduct;
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
