package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.state.order.OrderState;
import com.csipon.crm.domain.model.state.order.states.NewOrder;
import com.csipon.crm.domain.model.OrderStatus;

import java.time.LocalDateTime;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RealOrder implements Order {
    private Long id;
    private LocalDateTime date;
    private LocalDateTime preferedDate;
    private OrderStatus status;
    private User customer;
    private Product product;
    private User csr;
    private OrderState state;

    public RealOrder() {
        this.state = new NewOrder(this);
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
    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(OrderStatus status) {
        this.status = status;
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
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public User getCsr() {
        return csr;
    }

    @Override
    public void setCsr(User csr) {
        this.csr = csr;
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
    public LocalDateTime getPreferedDate() {
        return preferedDate;
    }

    @Override
    public void setPreferedDate(LocalDateTime preferedDate) {
        this.preferedDate = preferedDate;
    }

    @Override
    public OrderState getState() {
        return state;
    }

    @Override
    public void setState(OrderState state) {
        this.state = state;
    }
}