/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.utility;

import com.shawckz.reflex.Reflex;

public class ReflexException extends RuntimeException {

    public ReflexException() {
    }

    public ReflexException(String message) {
        super("Reflex v" + getVersion() + ": " + message);
    }

    public ReflexException(String message, Throwable cause) {
        super("Reflex v" + getVersion() + ": " + message, cause);
    }

    public ReflexException(Throwable cause) {
        super(cause);
    }

    public ReflexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("Reflex v" + getVersion() + ": " + message, cause, enableSuppression, writableStackTrace);
    }

    private static String getVersion() {
        return Reflex.getInstance().getDescription().getVersion();
    }

}
