/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.packet;

import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReflexPacketEvent extends Event {

    private final PacketEvent packetEvent;

    public ReflexPacketEvent(PacketEvent packetEvent) {
        this.packetEvent = packetEvent;
    }

    public PacketEvent getPacketEvent() {
        return packetEvent;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
