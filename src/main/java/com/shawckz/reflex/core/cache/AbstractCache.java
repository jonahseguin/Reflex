package com.shawckz.reflex.core.cache;

import com.shawckz.reflex.core.database.mongo.AutoMongo;
import com.shawckz.reflex.core.database.mongo.annotations.MongoColumn;
import com.mongodb.BasicDBObject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/*
 * Copyright (c) 2015 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

/**
 * Created by Jonah on 6/11/2015.
 */
public abstract class AbstractCache implements Listener {

    private final static ConcurrentMap<String, CachePlayer> players = new ConcurrentHashMap<>();
    private final static ConcurrentMap<String, CachePlayer> playersUUID = new ConcurrentHashMap<>();
    private final Plugin plugin;
    private final Class<? extends CachePlayer> aClass;

    public AbstractCache(Plugin plugin, Class<? extends CachePlayer> aClass){
        this.plugin = plugin;
        this.aClass = aClass;
        Bukkit.getServer().getPluginManager().registerEvents(this,plugin);
    }

    /**
     * Returns a CachePlayer instance if in local cache, if not in local cache this will attempt to load their CachePlayer object from the database
     * If they are not in the database or the name is null, will return null.
     *
     * @param name The Player's name to get a CachePlayer instance of
     *
     * @return The CachePlayer, null if not found in database && cache
     *
     * Please note that the name is case sensitive.
     */
    public CachePlayer getBasePlayer(String name){
        if(players.containsKey(name)){
            return players.get(name);
        }
        else{
            CachePlayer cp = loadCachePlayer(name);
            if(cp != null){
                return cp;
            }
            else{
                return null;
            }
        }
    }

    public CachePlayer getBasePlayerByUUID(String uuid) {
        if (playersUUID.containsKey(uuid)) {
            return playersUUID.get(uuid);
        } else {
            CachePlayer cp = loadCachePlayerByid(uuid);
            if (cp != null) {
                put(cp);
                return cp;
            } else {
                return null;
            }
        }
    }

    public CachePlayer loadCachePlayer(String name){
        String key = "username";
        for(Field f : aClass.getDeclaredFields()){
            MongoColumn row = f.getAnnotation(MongoColumn.class);
            if(row != null){
                if(row.identifier()){
                    if(row.name().equalsIgnoreCase("name") || f.getName().equalsIgnoreCase("username")){
                        key = row.name();
                        break;
                    }
                }
            }
        }

        List<AutoMongo> autoMongos = CachePlayer.select(new BasicDBObject(key,name),aClass);
        for(AutoMongo mongo : autoMongos){
            if(mongo instanceof CachePlayer){
                CachePlayer cachePlayer = (CachePlayer) mongo;
                return cachePlayer;
            }
        }
        return null;
    }

    public CachePlayer loadCachePlayerByid(String name){
        String key = "uniqueId";
        for(Field f : aClass.getDeclaredFields()){
            MongoColumn row = f.getAnnotation(MongoColumn.class);
            if(row != null){
                if(row.identifier()){
                    if(row.name().equalsIgnoreCase("uniqueId") || f.getName().equalsIgnoreCase("uuid")){
                        key = row.name();
                        break;
                    }
                }
            }
        }

        List<AutoMongo> autoMongos = CachePlayer.select(new BasicDBObject(key,name), aClass);
        for(AutoMongo mongo : autoMongos){
            if(mongo instanceof CachePlayer){
                CachePlayer cachePlayer = (CachePlayer) mongo;
                return cachePlayer;
            }
        }
        return null;
    }

    public CachePlayer getBasePlayer(Player p){
        return getBasePlayer(p.getName());
    }

    /**
     * Gets if the player by name is in the local cache
     *
     * @param name The Player's name
     *
     * @return true if in the cache, false if not
     */
    public boolean contains(String name){
        return players.containsKey(name);
    }

    /**
     * Adds a CachePlayer to the local cache.  Does not get cleared until server restart.
     *
     * @param cachePlayer The CachePlayer to add to the local cache
     */
    public void put(CachePlayer cachePlayer){
        players.put(cachePlayer.getName(),cachePlayer);
        playersUUID.put(cachePlayer.getUniqueId(), cachePlayer);
    }

    /**
     * Clear the local cache.
     * Used in onDisable to prevent memory leaks (due to the cache being static)
     */
    public static void clear(){
        players.clear();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCache(final AsyncPlayerPreLoginEvent e) {
        final String name = e.getName();
        final String uuid = e.getUniqueId().toString();
        CachePlayer cp = loadCachePlayer(e.getName());
        if(cp != null){
            put(cp);
        }
        else{
            cp = create(name,uuid);
            put(cp);
            cp.update();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(contains(p.getName())){
            CachePlayer cp = getBasePlayer(p);
            init(p, cp);
        }
    }

    public abstract CachePlayer create(String name, String uuid);

    public abstract void init(Player player,CachePlayer cachePlayer);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e){
        final Player p = e.getPlayer();
        save(p);
    }

    public void save(Player p){
        if(contains(p.getName())){
            final CachePlayer cachePlayer = (aClass.cast(getBasePlayer(p)));
            new BukkitRunnable(){
                @Override
                public void run() {
                    cachePlayer.update();
                }
            }.runTaskAsynchronously(plugin);
        }
    }

}
