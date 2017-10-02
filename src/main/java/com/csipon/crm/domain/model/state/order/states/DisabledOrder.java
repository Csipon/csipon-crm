package com.csipon.crm.domain.model.state.order.states;


import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.state.order.OrderState;

import static com.csipon.crm.domain.model.OrderStatus.*;

/**
 * Created by bpogo on 5/9/2017.
 */
public class DisabledOrder extends OrderState {
    public DisabledOrder(Order order) {
        super(order);
        this.stateName = DISABLED.getName();
        this.order.setStatus(DISABLED);
    }
}
