package com.csipon.crm.domain.model;

import java.time.LocalDateTime;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface History {

    Long getId();

    void setId(Long id);

    Status getNewStatus();

    void setNewStatus(Status newStatus);

    LocalDateTime getDateChangeStatus();

    void setDateChangeStatus(LocalDateTime dateChangeStatus);

    String getDescChangeStatus();

    void setDescChangeStatus(String descChangeStatus);

    Order getOrder();

    void setOrder(Order order);

    Complaint getComplaint();

    void setComplaint(Complaint complaint);

    Product getProduct();

    void setProduct(Product product);
}
