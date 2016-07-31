/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.internal;

import lombok.Getter;

import org.bukkit.entity.Player;

@Getter
public class ReflexAsyncMoveEvent extends ReflexInternalEvent {

    private final Player player;
    private final double x;
    private final double y;
    private final double z;
    private final boolean ground;

    public ReflexAsyncMoveEvent(Player player, double x, double y, double z, boolean ground) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ground = ground;
    }

}