package com.shawckz.reflex.player;

/*
 * Copyright (c) 2015 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */


import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.cache.AbstractCache;
import com.shawckz.reflex.cache.CachePlayer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by Jonah on 6/15/2015.
 */
public class ReflexCache extends AbstractCache {

    private static ReflexCache instance;

    private ReflexCache(Plugin plugin) {
        super(plugin, ReflexPlayer.class);
    }

    public static ReflexCache get() {
        if (instance == null) {
            synchronized (ReflexCache.class) {
                if (instance == null) {
                    instance = new ReflexCache(Reflex.getPlugin());
                }
            }
        }
        return instance;
    }

    //See superclass for documentation
    public ReflexPlayer getAresPlayer(String name){
        CachePlayer cachePlayer = getBasePlayer(name);
        if(cachePlayer != null) {
            return (ReflexPlayer) getBasePlayer(name);
        }
        return null;
    }

    public ReflexPlayer getAresPlayerByUUID(String uuid){
        CachePlayer cachePlayer = getBasePlayerByUUID(uuid);
        if(cachePlayer != null) {
            return (ReflexPlayer) getBasePlayerByUUID(uuid);
        }
        return null;
    }

    //See superclass for documentation
    public ReflexPlayer getAresPlayer(Player p){
        return getAresPlayer(p.getName());
    }

    @Override
    public CachePlayer create(String name, String uuid) {
        return new ReflexPlayer(name,uuid);
    }

    @Override
    public void init(Player p,CachePlayer cachePlayer) {
        if(cachePlayer instanceof ReflexPlayer){
            ReflexPlayer reflexPlayer = (ReflexPlayer) cachePlayer;
            reflexPlayer.setBukkitPlayer(p);
            if(p.hasPermission("ares.alerts")){
                reflexPlayer.setAlertsEnabled(true);
                p.sendMessage(Reflex.getPrefix() + " §7Alerts have been §aenabled§7.");
            }
            //TODO: Setup their shit.
        }
    }
}
