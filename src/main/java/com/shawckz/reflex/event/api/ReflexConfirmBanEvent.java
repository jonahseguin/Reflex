/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.backend.command.RCmdArgs;
import com.shawckz.reflex.ban.ReflexBan;

import org.bukkit.event.Cancellable;

/**
 * Called when the /reflex confirmban Command is called and a ban is confirmed on a player
 * Can be cancelled. {@link com.shawckz.reflex.commands.CmdBan#onCmdConfirmBan(RCmdArgs)}
 */
public class ReflexConfirmBanEvent extends ReflexAPIEvent implements Cancellable {

    private final ReflexBan ban;
    private boolean result;
    private boolean cancelled = false;

    public ReflexConfirmBanEvent(ReflexBan ban, boolean result) {
        this.ban = ban;
        this.result = result;
    }

    public ReflexBan getBan() {
        return ban;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
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
