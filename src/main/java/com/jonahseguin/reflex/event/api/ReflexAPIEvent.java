/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Superclass for all "API" events in Reflex; that can be utilized by third-party Developers
 * all classes in the {@link com.shawckz.reflex.event.api} package should extend this class {@link ReflexAPIEvent}
 */
public class ReflexAPIEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
