/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.player.reflex.ReflexPlayer;

public class ReflexPlayerSaveEvent extends ReflexAPIEvent {

    private final ReflexPlayer player;

    public ReflexPlayerSaveEvent(ReflexPlayer player) {
        this.player = player;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }
}
