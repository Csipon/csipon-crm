package com.csipon.crm.exception;

/**
 * Created by Pasha on 26.04.2017.
 */
public class IncorrectEmailElementException extends RuntimeException {
    public IncorrectEmailElementException() {
    }

    public IncorrectEmailElementException(String message) {
        super(message);
    }

    public IncorrectEmailElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectEmailElementException(Throwable cause) {
        super(cause);
    }

    public IncorrectEmailElementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
