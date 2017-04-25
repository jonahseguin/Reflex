/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.api;

import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.event.Cancellable;

/**
 * Called when a Trigger is called via {@link RTrigger}, can be cancelled
 */
public class ReflexTriggerEvent extends ReflexAPIEvent implements Cancellable {

    private final RTrigger trigger;
    private final ReflexPlayer player;
    private final CheckType checkType;
    private boolean cancelled = false;

    public ReflexTriggerEvent(RTrigger trigger, ReflexPlayer player, CheckType checkType) {
        this.trigger = trigger;
        this.player = player;
        this.checkType = checkType;
    }

    public RTrigger getTrigger() {
        return trigger;
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
