/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.internal;

import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;

@Getter
@Setter
public class ReflexAsyncMoveEvent extends ReflexInternalEvent {

    private final ReflexPlayer player;
    private final Location from;
    private final boolean ground;
    private Location to;

    public ReflexAsyncMoveEvent(ReflexPlayer player, Location to, Location from, boolean ground) {
        this.player = player;
        this.to = to;
        this.from = from;
        this.ground = ground;
    }

}
