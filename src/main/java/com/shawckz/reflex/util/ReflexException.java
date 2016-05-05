package com.shawckz.reflex.util;

public class ReflexException extends RuntimeException {

    public ReflexException() {
    }

    public ReflexException(String message) {
        super(message);
    }

    public ReflexException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflexException(Throwable cause) {
        super(cause);
    }

    public ReflexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
