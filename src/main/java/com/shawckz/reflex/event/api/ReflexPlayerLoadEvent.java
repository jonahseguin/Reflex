/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.player.reflex.ReflexPlayer;

/**
 * Called when a player joins the server and their ReflexPlayer along with all of their data is loaded from the database or cache.
 */
public class ReflexPlayerLoadEvent extends ReflexAPIEvent {

    private final ReflexPlayer player;

    public ReflexPlayerLoadEvent(ReflexPlayer player) {
        this.player = player;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }
}
