/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.auth;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.Configuration;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.util.utility.ReflexException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ShawXAuth extends Configuration {

    private static ShawXAuth sxa = null;

    @ConfigData("key")
    private String key = "xxx";

    public ShawXAuth(Reflex plugin) {
        super(plugin, "key.yml");

        if(sxa != null){
            throw new ReflexException("ShawXAuth has already been instantiated");
        }


        load();
        save();

        if(key.equals("xxx")) {
            Bukkit.getLogger().info(" ");
            Bukkit.getLogger().info("-------------------------------------");
            Bukkit.getLogger().info("Reflex - v" + Reflex.getInstance().getDescription().getVersion() + " by Shawckz");
            Bukkit.getLogger().info("https://shawckz.com/product/Reflex");
            Bukkit.getLogger().info(" ");
            Bukkit.getLogger().info("It appears this is your first time running Reflex, or your key has not been set in the key.yml file.");
            Bukkit.getLogger().info("Stop the server, (do not reload or use PluginManager!) and put your key in the key.yml file in plugins/Reflex.");
            Bukkit.getLogger().info("For more information on setting up authentication keys, visit https://shawckz.com/help#setup");
            Bukkit.getLogger().info(" ");
            Bukkit.getLogger().info("-------------------------------------");
            Bukkit.getLogger().info(" ");
        }

        sxa = this;
    }

    public static void log(String msg) {
        Bukkit.getLogger().info("[Reflex][Auth] " + msg);
    }

    public static String getKey() {
        return sxa.key;
    }

    public static void auth(final AuthMe me) {
        if(sxa == null) {
            new ShawXAuth(Reflex.getInstance());
        }
        final ShawXAuth shawXAuth = sxa;
        new BukkitRunnable(){
            @Override
            public void run() {
                final XSocketAuth auth = new XSocketAuth(shawXAuth);

                auth.auth(me, result -> {
                    if(result) {
                        me.doAuth(shawXAuth);
                        Bukkit.getLogger().info(" ");
                        Bukkit.getLogger().info("-------------------------------------");
                        Bukkit.getLogger().info("Reflex - v" + Reflex.getInstance().getDescription().getVersion() + " by Shawckz");
                        Bukkit.getLogger().info("https://shawckz.com/product/Reflex");
                        Bukkit.getLogger().info(" ");
                        Bukkit.getLogger().info("Authenticated successfully.  Thank you.");
                        Bukkit.getLogger().info("-------------------------------------");
                        Bukkit.getLogger().info(" ");
                    }
                    else{
                        Bukkit.getLogger().info(" ");
                        Bukkit.getLogger().info("-------------------------------------");
                        Bukkit.getLogger().info("Reflex - v" + Reflex.getInstance().getDescription().getVersion() + " by Shawckz");
                        Bukkit.getLogger().info("https://shawckz.com/product/Reflex");
                        Bukkit.getLogger().info(" ");
                        Bukkit.getLogger().info("AUTHENTICATION FAILURE - Reflex is not authorized to run on this server!");
                        Bukkit.getLogger().info(" ");
                        Bukkit.getLogger().info("If you purchased and paid for Reflex, you need to configure your authentication key in plugins/Reflex/key.yml");
                        Bukkit.getLogger().info("See https://shawckz.com/help#setup for more help on setting up your product.");
                        Bukkit.getLogger().info(" ");
                        Bukkit.getLogger().info("If you do not have an authentication key and/or obtained this copy of Reflex illegally, your IP has been logged and appropriate action will be taken.");
                        Bukkit.getLogger().info("-------------------------------------");
                        Bukkit.getLogger().info(" ");
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                Bukkit.getServer().getPluginManager().disablePlugin(me);
                                Reflex.couldStart = false;
                            }
                        }.runTask(Reflex.getInstance());
                    }
                });
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

}
