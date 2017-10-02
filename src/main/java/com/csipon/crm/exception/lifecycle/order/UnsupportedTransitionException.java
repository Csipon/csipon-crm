package com.csipon.crm.exception.lifecycle.order;

/**
 * Created by bpogo on 5/13/2017.
 */
public class UnsupportedTransitionException extends OrderLifecycleException {
    private String from;

    public UnsupportedTransitionException(String from) {
        super("Transition from " + from + " state to which you want is not allowed.");
        this.from = from;
    }

    public String getFrom() {
        return from;
    }
}
