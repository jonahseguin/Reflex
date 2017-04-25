/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.internal;

import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class ReflexAsyncMoveEvent extends ReflexInternalEvent {

    private final Player player;
    private final ReflexPlayer reflexPlayer;
    private final Location from;
    private final boolean ground;
    private Location to;

    public ReflexAsyncMoveEvent(Player player, ReflexPlayer reflexPlayer, Location to, Location from, boolean ground) {
        this.player = player;
        this.reflexPlayer = reflexPlayer;
        this.to = to;
        this.from = from;
        this.ground = ground;
    }

}
