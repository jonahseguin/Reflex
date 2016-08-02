/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.ban.ReflexBan;
import com.shawckz.reflex.player.reflex.ReflexPlayer;

import org.bukkit.event.Cancellable;

public class ReflexUnbanEvent extends ReflexAPIEvent implements Cancellable {

    private final ReflexPlayer player;
    private final ReflexBan ban;
    private boolean cancelled = false;

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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
