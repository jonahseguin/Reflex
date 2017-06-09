/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.listener;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.alert.AlertManager;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class BukkitListener implements Listener {

    private final Reflex instance;

    public BukkitListener(Reflex instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKick(PlayerKickEvent event) {
        if (event.isCancelled()) return;
        if (event.getReason().equalsIgnoreCase("Flying is not enabled on this server")) {
            AlertManager.staffMsg(ChatColor.BLUE + event.getPlayer().getName() + ChatColor.GRAY + " was kicked for flying.");
        }
    }

}
