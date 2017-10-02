package com.csipon.crm.dto;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 01.05.2017
 */
public class ComplaintDto {
    private Long id;
    private String title;
    private String message;
    private String status;
    private String date;
    private Long customerId;
    private Long pmgId;
    private Long orderId;

    public ComplaintDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPmgId() {
        return pmgId;
    }

    public void setPmgId(Long pmgId) {
        this.pmgId = pmgId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
