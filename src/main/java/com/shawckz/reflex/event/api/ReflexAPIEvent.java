/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReflexAPIEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
