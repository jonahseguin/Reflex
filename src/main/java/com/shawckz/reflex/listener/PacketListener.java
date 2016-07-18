/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.event.internal.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PacketListener {

    public PacketListener(Reflex instance) {
        //AsyncMoveEvent
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.POSITION) || event.getPacketType().equals(PacketType.Play.Client.POSITION_LOOK)) {
                    double x = event.getPacket().getDoubles().getValues().get(0);
                    double y = event.getPacket().getDoubles().getValues().get(1);
                    double z = event.getPacket().getDoubles().getValues().get(2);
                    boolean ground = event.getPacket().getBooleans().getValues().get(0);

                    ReflexAsyncMoveEvent moveEvent = new ReflexAsyncMoveEvent(event.getPlayer(), x, y, z, ground);
                    Bukkit.getPluginManager().callEvent(moveEvent);
                }
            }
        });
        //SwingEvent
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.ARM_ANIMATION) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.ARM_ANIMATION)) {
                    ReflexSwingEvent swingEvent = new ReflexSwingEvent(event.getPlayer());
                    Bukkit.getPluginManager().callEvent(swingEvent);
                }
            }
        });
        //UseEntityEvent
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.USE_ENTITY)) {
                    if (!event.getPlayer().getItemInHand().getType().equals(Material.FISHING_ROD)) {
                        ReflexUseEntityEvent useEntityEvent = new ReflexUseEntityEvent(event.getPlayer(), event.isCancelled());
                        Bukkit.getPluginManager().callEvent(useEntityEvent);
                    }
                }
            }
        });
        //FlyingEvent
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.FLYING) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.FLYING)) {
                    boolean ground = event.getPacket().getBooleans().getValues().get(0);
                    ReflexFlyingEvent flyingEvent = new ReflexFlyingEvent(event.getPlayer(), event.isCancelled(), ground);
                    Bukkit.getPluginManager().callEvent(flyingEvent);
                }
            }
        });
        /* FOR MINECRAFT 1.8, UNCOMMENT
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_VELOCITY) {
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Server.ENTITY_VELOCITY)) {
                     double x = event.getPacket().getIntegers().getValues().get(1);
                        double y = event.getPacket().getIntegers().getValues().get(2);
                        double z = event.getPacket().getIntegers().getValues().get(3);

                        x /= 8000.0D;
                        y /= 8000.0D;
                        z /= 8000.0D;

                        //Call ReflexVelocityEvent
                }
            }
        });
        */
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Server.EXPLOSION) {
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Server.EXPLOSION)) {
                    float x = event.getPacket().getFloat().getValues().get(1);
                    float y = event.getPacket().getFloat().getValues().get(2);
                    float z = event.getPacket().getFloat().getValues().get(3);

                    ReflexVelocityEvent velocityEvent = new ReflexVelocityEvent(event.getPlayer(), x, y, z);
                    Bukkit.getPluginManager().callEvent(velocityEvent);
                }
            }
        });
        instance.getProtocolManager().addPacketListener(new PacketAdapter(Reflex.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.LOOK) {
                    Player p = event.getPlayer();
                   // ReflexPlayer ap = Reflex.getInstance().getCache().getReflexPlayer(p);
                    float yaw = event.getPacket().getFloat().readSafely(0);
                    float pitch = event.getPacket().getFloat().readSafely(1);

                    ReflexLookEvent lookEvent = new ReflexLookEvent(p, yaw, pitch);
                    Bukkit.getPluginManager().callEvent(lookEvent);
                }
            }
        });
    }
}
