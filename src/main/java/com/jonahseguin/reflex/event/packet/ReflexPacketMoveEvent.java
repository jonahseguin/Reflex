/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.packet;

import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class ReflexPacketMoveEvent extends ReflexPacketEvent {

    private final Player player;
    private final ReflexPlayer reflexPlayer;
    private final Location from;
    private final boolean ground;
    private Location to;

    public ReflexPacketMoveEvent(PacketEvent packetEvent, Player player, ReflexPlayer reflexPlayer, Location to, Location from, boolean ground) {
        super(packetEvent);
        this.player = player;
        this.reflexPlayer = reflexPlayer;
        this.to = to;
        this.from = from;
        this.ground = ground;
    }

}
