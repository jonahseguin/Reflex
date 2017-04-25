/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.auth;


import org.bukkit.plugin.java.JavaPlugin;

public abstract class AuthMe extends JavaPlugin {

    public final void doAuth(ShawXAuth autheer) {
        if (autheer == null || !autheer.getClass().isAssignableFrom(ShawXAuth.class)) {
            throw new AuthException("Attempted to do auth from unknown ShawXAuth instance!");
        }
        auth(autheer).call();
    }

    public abstract AuthCaller auth(ShawXAuth autheer);

    public abstract String authName();

}
