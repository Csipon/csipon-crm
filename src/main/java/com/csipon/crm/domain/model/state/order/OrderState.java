/**
 * The MIT License
 * Copyright (c) 2014-2016 Ilkka Seppälä
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.csipon.crm.domain.model.state.order;


import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.OrderStatus;
import com.csipon.crm.domain.model.state.order.states.*;
import com.csipon.crm.domain.real.RealHistory;
import com.csipon.crm.exception.lifecycle.order.UnsupportedTransitionException;

import java.time.LocalDateTime;

/**
 * OrderState interface.
 */
public class OrderState {
    protected static final String DESC_ORDER_NEW = "New order was created.";
    protected static final String DESC_ORDER_PAUSED = "Order was paused.";
    protected static final String DESC_ORDER_ACTIVATED = "Order was successful activated and ready to use.";
    protected static final String DESC_ORDER_PROCESSING = "Order is assigned to CSR. Processing is started.";
    protected static final String DESC_ORDER_RESUMED = "Order was resumed and ready to use.";
    protected static final String DESC_REQUEST_TO_RESUME_ORDER = "Request to resume order.";
    protected static final String DESC_REQUEST_TO_PAUSE_ORDER = "Request to pause order.";
    protected static final String DESC_REQUEST_TO_DISABLE_ORDER = "Request to disable order.";

    protected String stateName;
    protected Order order;

    public OrderState(Order order) {
        this.order = order;
    }

    public History newOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    public History processOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    public History activateOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    public History pauseOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    public History resumeOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    public History disableOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    public History requestToResumeOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    public History requestToPauseOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    public History requestToDisableOrder() {
        throw new UnsupportedTransitionException(stateName);
    }

    protected Order getOrder() {
        return order;
    }

    protected History getOrderHistory(String description) {
        History history = new RealHistory();

        history.setOrder(this.order);
        history.setDateChangeStatus(LocalDateTime.now());
        history.setDescChangeStatus(description);
        history.setNewStatus(order.getStatus());

        return history;
    }

    /**
     * Use this method to set appropriate OrderState
     * when order is restoring (for example: from DB)
     *
     * @param status - order status
     * @param order  - restored object
     */
    public static void setStateForOrder(OrderStatus status, Order order) {
        switch (status) {
            case NEW:
                order.setState(new NewOrder(order));
                break;
            case PROCESSING:
                order.setState(new ProcessingOrder(order));
                break;
            case ACTIVE:
                order.setState(new ActiveOrder(order));
                break;
            case PAUSED:
                order.setState(new PausedOrder(order));
                break;
            case DISABLED:
                order.setState(new DisabledOrder(order));
                break;
            case REQUEST_TO_DISABLE:
                order.setState(new RequestToDisableOrder(order));
                break;
            case REQUEST_TO_PAUSE:
                order.setState(new RequestToPauseOrder(order));
                break;
            case REQUEST_TO_RESUME:
                order.setState(new RequestToResumeOrder(order));
                break;
            default:
                break;
        }
    }
}
