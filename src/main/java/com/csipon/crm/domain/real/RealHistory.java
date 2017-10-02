package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.*;
import com.netcracker.crm.domain.model.*;

import java.time.LocalDateTime;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RealHistory implements History {
    private Long id;
    private Status newStatus;
    private LocalDateTime dateChangeStatus;
    private String descChangeStatus;
    private Order order;
    private Complaint complaint;
    private Product product;

    public RealHistory() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Status getNewStatus() {
        return newStatus;
    }

    @Override
    public void setNewStatus(Status newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public LocalDateTime getDateChangeStatus() {
        return dateChangeStatus;
    }

    @Override
    public void setDateChangeStatus(LocalDateTime dateChangeStatus) {
        this.dateChangeStatus = dateChangeStatus;
    }

    @Override
    public String getDescChangeStatus() {
        return descChangeStatus;
    }

    @Override
    public void setDescChangeStatus(String descChangeStatus) {
        this.descChangeStatus = descChangeStatus;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public Complaint getComplaint() {
        return complaint;
    }

    @Override
    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }
}