package com.csipon.crm.service.entity;

import com.csipon.crm.domain.model.Order;

/**
 * Created by bpogo on 5/10/2017.
 */
public interface OrderLifecycleService {

    boolean createOrder(Order order);

    boolean processOrder(Long orderId, Long csrId);

    boolean activateOrder(Long orderId);

    boolean pauseOrder(Long orderId);

    boolean resumeOrder(Long orderId);

    boolean disableOrder(Long orderId);

    boolean requestToResumeOrder(Long orderId);

    boolean requestToPauseOrder(Long orderId);

    boolean requestToDisableOrder(Long orderId);
}
