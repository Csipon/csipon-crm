package com.csipon.crm.domain.model;

import java.time.LocalDateTime;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface Complaint {

    Long getId();

    void setId(Long id);
    
    String getTitle();

    void setTitle(String title);

    String getMessage();

    void setMessage(String message);

    ComplaintStatus getStatus();

    void setStatus(ComplaintStatus status);

    LocalDateTime getDate();

    void setDate(LocalDateTime date);

    User getCustomer();

    void setCustomer(User customer);

    User getPmg();

    void setPmg(User pmg);

    Order getOrder();

    void setOrder(Order order);

}
