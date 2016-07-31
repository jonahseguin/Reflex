/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.auth;

public abstract class XAutheer {

    private boolean authorized = false;

    public final boolean isAuthorized() {
        return authorized;
    }

    public final void authorize(XSocketAuth auth) {
        if (auth.isValid()) {
            authorized = true;
        }
    }

    public abstract void onAuth(boolean result);


}
