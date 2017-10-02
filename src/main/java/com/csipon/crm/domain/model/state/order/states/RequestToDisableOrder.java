package com.csipon.crm.domain.model.state.order.states;


import com.csipon.crm.domain.model.state.order.OrderState;
import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.Order;

import static com.csipon.crm.domain.model.OrderStatus.*;

/**
 * Created by bpogo on 5/9/2017.
 */
public class RequestToDisableOrder extends OrderState {

    public RequestToDisableOrder(Order order) {
        super(order);
        this.stateName = REQUEST_TO_DISABLE.getName().replace("_", " ");
        this.order.setStatus(REQUEST_TO_DISABLE);
    }

    @Override
    public History disableOrder() {
        order.setState(new DisabledOrder(order));

        return getOrderHistory("Order disabled.");
    }
}
