package com.csipon.crm.domain.model.state.order.states;

import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.state.order.OrderState;

import static com.csipon.crm.domain.model.OrderStatus.*;

/**
 * Created by bpogo on 5/9/2017.
 */
public class ProcessingOrder extends OrderState {

    public ProcessingOrder(Order order) {
        super(order);
        this.stateName = PROCESSING.getName();
        this.order.setStatus(PROCESSING);
    }

    @Override
    public History activateOrder() {
        order.setState(new ActiveOrder(order));

        return getOrderHistory(DESC_ORDER_ACTIVATED);
    }
}
