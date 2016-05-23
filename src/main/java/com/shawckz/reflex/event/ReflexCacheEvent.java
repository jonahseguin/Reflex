/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event;

import com.shawckz.reflex.player.reflex.ReflexPlayer;

public class ReflexCacheEvent extends ReflexEvent {

    private final ReflexPlayer player;

    public ReflexCacheEvent(ReflexPlayer player) {
        this.player = player;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }
}
