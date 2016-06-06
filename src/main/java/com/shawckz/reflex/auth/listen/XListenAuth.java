/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.auth.listen;

import com.shawckz.reflex.auth.XAutheer;
import com.shawckz.reflex.auth.XSocketAuth;
import io.socket.emitter.Emitter;

public class XListenAuth implements Emitter.Listener {

    private final XAutheer autheer;
    private final XSocketAuth socketAuth;

    public XListenAuth(XAutheer autheer, XSocketAuth socketAuth) {
        this.autheer = autheer;
        this.socketAuth = socketAuth;
    }

    public final void call(Object... objects) {
        if(objects != null && objects.length > 0) {
            String result = (String) objects[0];
            if(result.equalsIgnoreCase("true")) {
                autheer.authorize(socketAuth);
                autheer.onAuth(true);
            }
            else{
                autheer.onAuth(false);
            }
        }
    }
}
