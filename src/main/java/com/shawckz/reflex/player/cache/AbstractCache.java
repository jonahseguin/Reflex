/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.player.cache;

import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.event.api.ReflexPlayerLoadEvent;
import com.shawckz.reflex.event.api.ReflexPlayerSaveEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import org.bson.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah on 6/11/2015.
 */
public abstract class AbstractCache implements Listener {

    private final ConcurrentMap<String, ReflexPlayer> players = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ReflexPlayer> playersUUID = new ConcurrentHashMap<>();
    private final Set<Player> onlinePlayers = new HashSet<>();
    private final Plugin plugin;
    private final Class<? extends ReflexPlayer> aClass;

    public AbstractCache(Plugin plugin) {
        this.plugin = plugin;
        this.aClass = ReflexPlayer.class;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    protected ConcurrentMap<String, ReflexPlayer> getPlayers() {
        return players;
    }

    public Set<Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    /**
     * Returns a ReflexPlayer instance if in local cache, if not in local cache this will attempt to load their ReflexPlayer object from the database
     * If they are not in the database or the name is null, will return null.
     *
     * @param name The Player's name to get a ReflexPlayer instance of
     *
     * @return The ReflexPlayer, null if not found in database && cache
     *
     * Please note that the name is case sensitive.
     */
    protected ReflexPlayer getBasePlayer(String name) {
        if (players.containsKey(name)) {
            return players.get(name);
        }
        else {
            ReflexPlayer cp = loadReflexPlayer(name);
            if (cp != null) {
                return cp;
            }
            else {
                return null;
            }
        }
    }

    protected ReflexPlayer getBasePlayerByUUID(String uuid) {
        if (playersUUID.containsKey(uuid)) {
            return playersUUID.get(uuid);
        }
        else {
            ReflexPlayer cp = loadReflexPlayerById(uuid);
            if (cp != null) {
                put(cp);
                return cp;
            }
            else {
                return null;
            }
        }
    }

    public ReflexPlayer loadReflexPlayer(String name) {
        final String key = "username"; //Matching the field in ReflexPlayer
        List<AutoMongo> autoMongos = ReflexPlayer.select(new Document(key, name), aClass);
        for (AutoMongo mongo : autoMongos) {
            if (mongo instanceof ReflexPlayer) {
                ReflexPlayer ReflexPlayer = (ReflexPlayer) mongo;
                return ReflexPlayer;
            }
        }
        return null;
    }

    public ReflexPlayer loadReflexPlayerById(String id) {
        final String key = "uuid"; //Matching the field in ReflexPlayer
        List<AutoMongo> autoMongos = ReflexPlayer.select(new Document(key, id), aClass);
        for (AutoMongo mongo : autoMongos) {
            if (mongo instanceof ReflexPlayer) {
                ReflexPlayer ReflexPlayer = (ReflexPlayer) mongo;
                return ReflexPlayer;
            }
        }
        return null;
    }

    public ReflexPlayer getBasePlayer(Player p) {
        return getBasePlayer(p.getName());
    }

    /**
     * Gets if the player by name is in the local cache
     *
     * @param name The Player's name
     *
     * @return true if in the cache, false if not
     */
    public boolean contains(String name) {
        return players.containsKey(name);
    }

    /**
     * Adds a ReflexPlayer to the local cache.  Does not get cleared until server restart.
     *
     * @param ReflexPlayer The ReflexPlayer to add to the local cache
     */
    public void put(ReflexPlayer ReflexPlayer) {
        players.put(ReflexPlayer.getName(), ReflexPlayer);
        playersUUID.put(ReflexPlayer.getUniqueId(), ReflexPlayer);
    }

    /**
     * Clear the local cache.
     * Used in onDisable to prevent memory leaks (due to the cache being static)
     */
    public void clear() {
        players.clear();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCache(final AsyncPlayerPreLoginEvent e) {
        final String name = e.getName();
        final String uuid = e.getUniqueId().toString();
        ReflexPlayer cp = loadReflexPlayer(e.getName());
        if (cp != null) {
            put(cp);
        }
        else {
            cp = create(name, uuid);
            put(cp);
            cp.update();
        }
        plugin.getServer().getPluginManager().callEvent(new ReflexPlayerLoadEvent(cp));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        if (contains(p.getName())) {
            ReflexPlayer cp = getBasePlayer(p);
            cp.setOnline(true);
            init(p, cp);
        }
        onlinePlayers.add(p);
    }

    public abstract ReflexPlayer create(String name, String uuid);

    public abstract void init(Player player, ReflexPlayer ReflexPlayer);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        if (contains(p.getName())) {
            ReflexPlayer reflexPlayer = getBasePlayer(p);
            reflexPlayer.setOnline(false);
            save(p);
        }
        if (onlinePlayers.contains(p)) {
            onlinePlayers.remove(p);
        }
    }

    public void save(final Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                saveSync(p);
            }
        }.runTaskAsynchronously(plugin);
    }

    public void saveSync(Player p) {
        if (contains(p.getName())) {
            final ReflexPlayer reflexPlayer = getBasePlayer(p);
            reflexPlayer.update();
            plugin.getServer().getPluginManager().callEvent(new ReflexPlayerSaveEvent(reflexPlayer));
        }
    }

}
