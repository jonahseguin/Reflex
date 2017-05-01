/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.packet;

import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

/**
 * Created by Jonah Seguin on Sun 2017-04-30 at 17:42.
 * Project: Reflex
 */
public class ReflexPacketKeepAliveEvent extends ReflexPacketEvent {

    private final ReflexPlayer player;

    public ReflexPacketKeepAliveEvent(PacketEvent packetEvent, ReflexPlayer player) {
        super(packetEvent);
        this.player = player;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }
}
