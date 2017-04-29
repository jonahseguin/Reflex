/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.listener;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.event.internal.ReflexVelocityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

public class BukkitListener implements Listener {

    private final Reflex instance;

    public BukkitListener(Reflex instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        ReflexVelocityEvent reflexVelocityEvent = new ReflexVelocityEvent(
                e.getPlayer(),
                e.getVelocity().getX(),
                e.getVelocity().getY(),
                e.getVelocity().getZ());
        instance.getServer().getPluginManager().callEvent(reflexVelocityEvent);
    }

}
