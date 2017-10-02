package com.csipon.crm.domain.model.state.order.states;


import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.state.order.OrderState;

import static com.csipon.crm.domain.model.OrderStatus.*;

/**
 * Created by bpogo on 5/9/2017.
 */
public class PausedOrder extends OrderState {

    public PausedOrder(Order order) {
        super(order);
        this.stateName = PAUSED.getName();
        this.order.setStatus(PAUSED);
    }

    @Override
    public History requestToResumeOrder() {
        order.setState(new RequestToResumeOrder(order));

        return getOrderHistory(DESC_REQUEST_TO_RESUME_ORDER);
    }

    @Override
    public History requestToDisableOrder() {
        order.setState(new RequestToDisableOrder(order));

        return getOrderHistory(DESC_REQUEST_TO_DISABLE_ORDER);
    }
}
