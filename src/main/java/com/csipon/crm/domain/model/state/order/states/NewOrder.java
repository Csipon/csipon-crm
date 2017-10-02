package com.csipon.crm.domain.model.state.order.states;

import com.csipon.crm.domain.model.state.order.OrderState;
import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.Order;

import static com.csipon.crm.domain.model.OrderStatus.*;

/**
 * Created by bpogo on 5/9/2017.
 */
public class NewOrder extends OrderState {

    public NewOrder(Order order) {
        super(order);
        this.stateName = NEW.getName();
        this.order.setStatus(NEW);
    }

    @Override
    public History newOrder() {
        return getOrderHistory(DESC_ORDER_NEW);
    }

    @Override
    public History processOrder() {
        order.setState(new ProcessingOrder(order));
        return getOrderHistory(DESC_ORDER_PROCESSING);
    }
}
