package com.csipon.crm.exception.lifecycle.order;

/**
 * Created by bpogo on 5/13/2017.
 */
public class OrderLifecycleException extends RuntimeException {
    public OrderLifecycleException() {
    }

    public OrderLifecycleException(String message) {
        super(message);
    }

    public OrderLifecycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderLifecycleException(Throwable cause) {
        super(cause);
    }

    public OrderLifecycleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
