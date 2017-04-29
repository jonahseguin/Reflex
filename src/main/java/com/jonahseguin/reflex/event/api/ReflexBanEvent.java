/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.api;

import com.jonahseguin.reflex.ban.ReflexBan;
import org.bukkit.event.Cancellable;

/**
 * Called when a player is banned by a ReflexBan, and can be cancelled.
 * Is NOT called when a played is automatically banned whilst the plugin is in a non-REFLEX ban mode (via configuration)
 */
public class ReflexBanEvent extends ReflexAPIEvent implements Cancellable {

    private final ReflexBan ban;
    private boolean cancelled = false;

    public ReflexBanEvent(ReflexBan ban) {
        this.ban = ban;
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
