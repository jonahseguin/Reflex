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
import com.shawckz.reflex.player.reflex.ReflexPlayer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PacketListener {

    public PacketListener(final Reflex instance) {
        //AsyncMoveEvent
        instance.getProtocolManager().getAsynchronousManager().registerAsyncHandler(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.POSITION_LOOK) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.POSITION_LOOK)) {
                    ReflexPlayer reflexPlayer = instance.getCache().getReflexPlayer(event.getPlayer());
                    World world = event.getPlayer().getWorld();
                    double x = event.getPacket().getDoubles().getValues().get(0);
                    double y = event.getPacket().getDoubles().getValues().get(1);
                    double z = event.getPacket().getDoubles().getValues().get(2);
                    float yaw = event.getPacket().getFloat().getValues().get(0);
                    float pitch = event.getPacket().getFloat().getValues().get(1);
                    boolean ground = event.getPacket().getBooleans().getValues().get(0);

                    Location to = new Location(world, x, y, z, yaw, pitch);
                    Location from = reflexPlayer.getData().getFrom();
                    if (from == null) {
                        reflexPlayer.getData().setFrom(to);
                        from = to;
                    }

                    ReflexAsyncMoveEvent moveEvent = new ReflexAsyncMoveEvent(reflexPlayer, to, from, ground);
                    instance.getServer().getPluginManager().callEvent(moveEvent);

                    x = moveEvent.getTo().getX();
                    y = moveEvent.getTo().getY();
                    z = moveEvent.getTo().getZ();

                    event.getPacket().getDoubles().getValues().set(0, x);
                    event.getPacket().getDoubles().getValues().set(1, y);
                    event.getPacket().getDoubles().getValues().set(2, z);

                    reflexPlayer.getData().setFrom(to);
                }
            }
        }).start();
        //SwingEvent
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.ARM_ANIMATION) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.ARM_ANIMATION)) {
                    ReflexSwingEvent swingEvent = new ReflexSwingEvent(event.getPlayer());
                    instance.getServer().getPluginManager().callEvent(swingEvent);
                }
            }
        });
        //UseEntityEvent
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.USE_ENTITY)) {
                    if (!event.getPlayer().getItemInHand().getType().equals(Material.FISHING_ROD)) {
                        ReflexUseEntityEvent useEntityEvent = new ReflexUseEntityEvent(event.getPlayer(), event.isCancelled());
                        instance.getServer().getPluginManager().callEvent(useEntityEvent);
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
                    instance.getServer().getPluginManager().callEvent(flyingEvent);
                }
            }
        });
        //VelocityEvent
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Server.EXPLOSION) {
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Server.EXPLOSION)) {
                    float x = event.getPacket().getFloat().getValues().get(1);
                    float y = event.getPacket().getFloat().getValues().get(2);
                    float z = event.getPacket().getFloat().getValues().get(3);

                    ReflexVelocityEvent velocityEvent = new ReflexVelocityEvent(event.getPlayer(), x, y, z);
                    instance.getServer().getPluginManager().callEvent(velocityEvent);
                }
            }
        });
        //LookEvent
        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL,
                PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.LOOK) {
                    Player p = event.getPlayer();
                    // ReflexPlayer ap = Reflex.getInstance().getCache().getReflexPlayers(p);
                    float yaw = event.getPacket().getFloat().readSafely(0);
                    float pitch = event.getPacket().getFloat().readSafely(1);

                    ReflexLookEvent lookEvent = new ReflexLookEvent(p, yaw, pitch);
                    instance.getServer().getPluginManager().callEvent(lookEvent);
                }
            }
        });
    }
}
