package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.*;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class RealHistory implements History {
    @Id
    @GeneratedValue
    private Long id;
    @JoinColumn
    private Status newStatus;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateChangeStatus;
    @Column
    private String descChangeStatus;
    @JoinColumn(name = "order_id", table = "orders", referencedColumnName = "id")
    private Order order;
    @JoinColumn(name = "complaint_id", table = "complaint", referencedColumnName = "id")
    private Complaint complaint;
    @JoinColumn(name = "product_id", table = "product", referencedColumnName = "id")
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