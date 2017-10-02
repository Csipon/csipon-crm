package com.csipon.crm.domain.model;

import com.csipon.crm.domain.model.state.order.OrderState;

import java.time.LocalDateTime;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface Order {

    Long getId();

    void setId(Long id);

    OrderStatus getStatus();

    void setStatus(OrderStatus status);

    User getCustomer();

    void setCustomer(User customer);

    Product getProduct();

    void setProduct(Product product);

    User getCsr();

    void setCsr(User csr);

    LocalDateTime getDate();

    void setDate(LocalDateTime date);

    LocalDateTime getPreferedDate();

    void setPreferedDate(LocalDateTime preferedDate);

    OrderState getState();

    void setState(OrderState state);
}
