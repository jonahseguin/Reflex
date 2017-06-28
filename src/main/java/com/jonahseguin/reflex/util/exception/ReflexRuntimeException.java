/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.exception;

import com.jonahseguin.reflex.Reflex;

public class ReflexRuntimeException extends RuntimeException {

    public ReflexRuntimeException() {
    }

    public ReflexRuntimeException(String message) {
        super("Reflex v" + getVersion() + ": " + message);
    }

    public ReflexRuntimeException(String message, Throwable cause) {
        super("Reflex v" + getVersion() + ": " + message, cause);
    }

    public ReflexRuntimeException(Throwable cause) {
        super(cause);
    }

    public ReflexRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("Reflex v" + getVersion() + ": " + message, cause, enableSuppression, writableStackTrace);
    }

    private static String getVersion() {
        return Reflex.getInstance().getDescription().getVersion();
    }

}
