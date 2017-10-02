package com.csipon.crm.domain.model.state.order.states;


import com.csipon.crm.domain.model.state.order.OrderState;
import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.Order;

import static com.csipon.crm.domain.model.OrderStatus.*;

/**
 * Created by bpogo on 5/9/2017.
 */
public class RequestToPauseOrder extends OrderState {

    public RequestToPauseOrder(Order order) {
        super(order);
        this.stateName = REQUEST_TO_PAUSE.getName().replace("_", " ");
        this.order.setStatus(REQUEST_TO_PAUSE);
    }

    @Override
    public History pauseOrder() {
        order.setState(new PausedOrder(order));

        return getOrderHistory(DESC_ORDER_PAUSED);
    }
}
