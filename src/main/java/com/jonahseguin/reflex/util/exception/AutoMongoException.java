/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.exception;

/**
 * Created by Jonah Seguin on Mon 2017-05-29 at 09:29.
 * Project: Reflex
 */
public class AutoMongoException extends ReflexException {

    public AutoMongoException() {
    }

    public AutoMongoException(String message) {
        super(message);
    }

    public AutoMongoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutoMongoException(Throwable cause) {
        super(cause);
    }

    public AutoMongoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
