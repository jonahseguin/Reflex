/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Data;
import lombok.NonNull;

import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:34.
 * Project: Reflex
 */
@Data
public class CheckResult {

    private final CheckType checkType;
    private final ReflexPlayer player;
    @NonNull
    private boolean cancelled;
    @NonNull
    private boolean canCancel;

    public CheckResult doCancel() {
        this.cancelled = true;
        return this;
    }

    public boolean canCancel() {
        return canCancel;
    }

    public CheckResult cancelIfAllowed(Cancellable cancellable) {
        if (canCancel) {
            cancellable.setCancelled(true);
            if (cancellable instanceof PlayerMoveEvent) {
                ((PlayerMoveEvent) cancellable).setTo(((PlayerMoveEvent) cancellable).getFrom());
            }
            cancelled = true;
        }
        return this;
    }

}
