/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.backend.command;

/**
 * Created by Jonah Seguin on 12/13/2015.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class RCommandException extends RuntimeException {

    public RCommandException() {
    }

    public RCommandException(String message) {
        super(message);
    }

    public RCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public RCommandException(Throwable cause) {
        super(cause);
    }

    public RCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
