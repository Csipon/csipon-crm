package com.csipon.crm.domain.real;

import com.csipon.crm.domain.converter.ComplaintStatusConverter;
import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.model.ComplaintStatus;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "complaint")
public class RealComplaint implements Complaint {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String title;
    @Column
    private String message;
    @Convert(converter = ComplaintStatusConverter.class)
    @Column(name = "status_id")
    private ComplaintStatus status;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;
    @JoinColumn(name = "customer_id", table = "users", referencedColumnName = "id")
    private User customer;
    @JoinColumn(name = "pmg_id", table = "users", referencedColumnName = "id")
    private User pmg;
    @JoinColumn(name = "order_id", table = "orders", referencedColumnName = "id")
    private Order order;

    public RealComplaint() {
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
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public ComplaintStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    @Override
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public User getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(User customer) {
        this.customer = customer;
    }

    @Override
    public User getPmg() {
        return pmg;
    }

    @Override
    public void setPmg(User pmg) {
        this.pmg = pmg;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
    }
}