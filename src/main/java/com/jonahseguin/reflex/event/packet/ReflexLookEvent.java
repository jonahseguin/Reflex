/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.packet;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

public class ReflexLookEvent extends ReflexPacketEvent {

    private final Player player;
    private final float yaw;
    private final float pitch;

    public ReflexLookEvent(PacketEvent packetEvent, Player player, float yaw, float pitch) {
        super(packetEvent);
        this.player = player;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Player getPlayer() {
        return player;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
