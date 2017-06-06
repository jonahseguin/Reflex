/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.obj;

import com.jonahseguin.reflex.Reflex;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 360 on 3/29/2015.
 */
public class Freeze implements Listener {

    private static Map<String, Freeze> freezes = new HashMap<>();
    private String name;
    private Player p;
    private boolean cancelled;
    private Location freeze;

    public Freeze(Player p) {
        this.p = p;
        this.name = p.getName();
        this.cancelled = false;
        this.freeze = p.getLocation();
        freezes.put(p.getName(), this);
    }

    public Freeze(Player p, Location freeze) {
        this.p = p;
        this.name = p.getName();
        this.cancelled = false;
        this.freeze = freeze;
        freezes.put(p.getName(), this);
    }

    public static Freeze getFreeze(Player p) {
        return freezes.get(p.getName());
    }

    public static boolean hasFreeze(Player p) {
        if (freezes.containsKey(p.getName())) {
            if (!freezes.get(p.getName()).isCancelled()) {
                return true;
            } else {
                freezes.remove(p.getName());
                return false;
            }
        }
        return false;
    }

    public static void removeFreeze(Player p) {
        if (freezes.containsKey(p.getName())) {
            if (hasFreeze(p)) {
                freezes.get(p.getName()).setCancelled(true);
                freezes.remove(p.getName());
            } else {
                freezes.remove(p.getName());
            }
        }
    }

    public static void sendWallBlock(Player p, Location loc) {
        p.sendBlockChange(loc, Material.STAINED_GLASS, DyeColor.RED.getData());
    }

    public static void removeWallBlock(Player p, Location loc) {
        p.sendBlockChange(loc, loc.getWorld().getBlockAt(loc).getType(), loc.getWorld().getBlockAt(loc).getData());
    }

    public void run() {
        Reflex.getInstance().getReflexPluginManager().registerEvents(this, Reflex.getInstance());
        for (Location loc : getWallBlocks(freeze)) {
            sendWallBlock(p, loc);
        }
    }

    public List<Location> getWallBlocks(final Location loc) {

        final Location l = loc.clone();

        List<Location> locations = new ArrayList<>();
        locations.add(l.clone().subtract(0, 1, 0));
        locations.add(l.clone().add(0, 2, 0));
        locations.add(l.clone().add(1, 1, 0));
        locations.add(l.clone().clone().add(0, 1, 1));
        locations.add(l.clone().add(-1, 1, 0));
        locations.add(l.clone().add(0, 1, -1));

        locations.add(l.clone().add(1, 0, 0));
        locations.add(l.clone().clone().add(0, 0, 1));
        locations.add(l.clone().add(-1, 0, 0));
        locations.add(l.clone().add(0, 0, -1));

        return locations;
    }

    public Player getPlayer() {
        return p;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        if (cancelled) {
            if (Bukkit.getPlayer(name) != null) {
                Player p = Bukkit.getPlayer(name);
                for (Location loc : getWallBlocks(freeze)) {
                    removeWallBlock(p, loc);
                }
            }
        }
    }

    public Location getFreeze() {
        return freeze;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (cancelled) {
            HandlerList.unregisterAll(this);
            return;
        }
        if (Bukkit.getPlayer(name) != null) {
            Player p = Bukkit.getPlayer(name);
            if (e.getPlayer().getName().equals(p.getName())) {

                for (Location loc : getWallBlocks(freeze)) {
                    sendWallBlock(e.getPlayer(), loc);
                }

                if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockY() == e.getFrom().getBlockY() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) {
                    return;
                }
                e.setTo(freeze);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        if (cancelled) {
            if (Bukkit.getPlayer(name) != null) {
                Player p = Bukkit.getPlayer(name);
                this.p = p;
                if (e.getPlayer().getName().equals(p.getName())) {
                    e.getPlayer().setAllowFlight(false);
                    for (Location loc : getWallBlocks(freeze)) {
                        removeWallBlock(e.getPlayer(), loc);
                    }
                    HandlerList.unregisterAll(this);
                }
            }
        } else {
            if (Bukkit.getPlayer(name) != null) {
                Player p = Bukkit.getPlayer(name);
                if (e.getPlayer().getName().equals(p.getName())) {
                    e.getPlayer().teleport(freeze);
                    for (Location loc : getWallBlocks(freeze)) {
                        sendWallBlock(e.getPlayer(), loc);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (cancelled) {
            HandlerList.unregisterAll(this);
        } else {
            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                if (p.getName().equalsIgnoreCase(name)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (cancelled) {
            HandlerList.unregisterAll(this);
        } else {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();

                if (p.getName().equalsIgnoreCase(name)) {
                    e.setDamage(0.0);
                    e.setCancelled(true);
                }

            }
        }
    }

}
