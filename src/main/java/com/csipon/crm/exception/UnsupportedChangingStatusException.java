package com.csipon.crm.exception;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.05.2017
 */

public class UnsupportedChangingStatusException  extends RuntimeException {
    public UnsupportedChangingStatusException() {
    }

    public UnsupportedChangingStatusException(String message) {
        super(message);
    }

    public UnsupportedChangingStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedChangingStatusException(Throwable cause) {
        super(cause);
    }

    public UnsupportedChangingStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
