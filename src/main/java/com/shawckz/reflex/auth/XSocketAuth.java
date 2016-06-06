/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.auth;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.auth.listen.XListenAuth;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class XSocketAuth {

    private final String uri = "https://shawckz.com";

    private Socket socket;

    private static XSocketAuth x = null;

    private boolean authed = false;

    public XSocketAuth(ShawXAuth instance) {
        if (instance == null || !instance.getClass().isAssignableFrom(ShawXAuth.class)) {
            error("Attempted to instantiate XSocketAuth from unknown class!");
            throw new AuthException("Attempted to instantiate XSocketAuth from unknown class!");
        }

        if(x != null) {
            error("Attempted to create XSocketAuth externally");
            throw new AuthException("Attempted to create XSocketAuth externally!");
        }
        else {
            x = this;
        }
    }

    public final void auth(final AuthMe authMe, final AuthResult r) {
        ShawXAuth.log("Attempting to connect to authentication service...");
        try {
            socket = IO.socket(uri).connect();
            while(!socket.connected()) {}
            ShawXAuth.log("Connection successful, waiting for response");

            new BukkitRunnable(){
                @Override
                public void run() {
                    if(!authed) {
                        error("Authenticating took too long");
                    }
                }
            }.runTaskLater(Reflex.getInstance(), (20 * 30));

        }
        catch (Exception ex){
            r.auth(false);
            error("x0.1-2 Unable to connect to authentication service");
        }

        socket.once(XAuthEvent.AUTHORIZE_RESULT.getName(), new XListenAuth(new XAutheer() {
            @Override
            public void onAuth(boolean result) {
                r.auth(result);
                authed = result;
            }
        }, this));

        try {
            JSONObject args = new JSONObject()
                    .append("ip", Bukkit.getServer().getIp())
                    .append("port", Bukkit.getServer().getPort())
                    .append("servername", Bukkit.getServer().getServerName())
                    .append("key", ShawXAuth.getKey())
                    .append("product", authMe.authName());
            socket.emit(XAuthEvent.AUTHORIZE_REQUEST.getName(), args);

           /* socket.on(XAuthEvent.AUTHORIZE_RESULT.getName(), new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    ShawXAuth.log("Received object (manual)");
                    if (objects != null && objects.length > 0) {
                        String result = (String) objects[0];
                        if (result.equalsIgnoreCase("true")) {
                            r.auth(true);
                        }
                        else {
                            r.auth(false);
                        }
                    }
                }
            });*/
        }
        catch (JSONException ex) {
            error("x0.1-1 Exception while authenticating");
        }
    }

    public final boolean isValid() {
        return socket.connected();
    }

    private void error(String msg) {
        Bukkit.getLogger().info("-------------------------------------");
        Bukkit.getLogger().info("Reflex - v" + Reflex.getInstance().getDescription().getVersion() + " by Shawckz");
        Bukkit.getLogger().info("https://shawckz.com/product/Reflex");
        Bukkit.getLogger().info(" ");
        Bukkit.getLogger().info("Reflex was unable to start (Authentication error)");
        Bukkit.getLogger().info("Message: " + msg);
        Bukkit.getLogger().info("If you get this issue consistently, please open a ticket at https://shawckz.com/tickets, or via contact at https://shawckz.com/contact");
        Bukkit.getLogger().info("-------------------------------------");

        Bukkit.getPluginManager().disablePlugin(Reflex.getInstance());
    }

}
