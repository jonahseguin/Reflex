/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.api;

import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.event.Cancellable;

/**
 * Called when a player fails a check
 */
public class ReflexFailEvent extends ReflexAPIEvent implements Cancellable {

    private final ReflexPlayer player;
    private final CheckType checkType;
    private boolean cancelled = false;

    public ReflexFailEvent(ReflexPlayer player, CheckType checkType) {
        this.player = player;
        this.checkType = checkType;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }

    public CheckType getCheckType() {
        return checkType;
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
