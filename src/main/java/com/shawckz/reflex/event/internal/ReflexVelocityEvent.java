/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.internal;

import lombok.Getter;

import org.bukkit.entity.Player;

@Getter
public class ReflexVelocityEvent extends ReflexInternalEvent {

    private final Player player;
    private final double x;
    private final double y;
    private final double z;

    public ReflexVelocityEvent(Player player, double x, double y, double z) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
