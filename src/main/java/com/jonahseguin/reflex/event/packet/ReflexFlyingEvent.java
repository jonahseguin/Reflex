/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.packet;

import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;

import org.bukkit.entity.Player;

@Getter
public class ReflexFlyingEvent extends ReflexPacketEvent {

    private final Player player;
    private final boolean ground;

    public ReflexFlyingEvent(PacketEvent packetEvent, Player player, boolean ground) {
        super(packetEvent);
        this.player = player;
        this.ground = ground;
    }
}
