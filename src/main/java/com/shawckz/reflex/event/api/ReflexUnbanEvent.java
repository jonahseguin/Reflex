/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.ban.ReflexBan;
import com.shawckz.reflex.player.reflex.ReflexPlayer;

public class ReflexUnbanEvent extends ReflexAPIEvent {

    private final ReflexPlayer player;
    private final ReflexBan ban;

    public ReflexUnbanEvent(ReflexPlayer player, ReflexBan ban) {
        this.player = player;
        this.ban = ban;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }

    public ReflexBan getBan() {
        return ban;
    }
}
