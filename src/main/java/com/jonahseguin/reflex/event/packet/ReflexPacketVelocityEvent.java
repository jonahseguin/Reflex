/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.packet;

import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;

import org.bukkit.entity.Player;

@Getter
public class ReflexPacketVelocityEvent extends ReflexPacketEvent {

    private final Player player;
    private final double x;
    private final double y;
    private final double z;

    public ReflexPacketVelocityEvent(PacketEvent packetEvent, Player player, double x, double y, double z) {
        super(packetEvent);
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
