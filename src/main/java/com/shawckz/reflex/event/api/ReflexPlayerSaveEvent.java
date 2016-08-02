/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;

/**
 * Called when a local {@link ReflexPlayer} is saved to the database via {@link com.shawckz.reflex.player.cache.AbstractCache#saveSync(Player)},
 * and all other save methods, in ReflexCache aswell
 */
public class ReflexPlayerSaveEvent extends ReflexAPIEvent {

    private final ReflexPlayer player;

    public ReflexPlayerSaveEvent(ReflexPlayer player) {
        this.player = player;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }
}
