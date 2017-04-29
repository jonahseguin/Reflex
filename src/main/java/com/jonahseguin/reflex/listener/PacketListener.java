/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.event.internal.*;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PacketListener {

    public PacketListener(final Reflex instance) {
        //AsyncMoveEvent
        instance.getProtocolManager().getAsynchronousManager().registerAsyncHandler(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.POSITION, PacketType.Play.Client.LOOK) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType().equals(PacketType.Play.Client.POSITION_LOOK) || event.getPacketType().equals(PacketType.Play.Client.POSITION) || event.getPacketType().equals(PacketType.Play.Client.LOOK)) {
                    ReflexPlayer reflexPlayer = instance.getCache().getReflexPlayer(event.getPlayer());
                    Location location = event.getPlayer().getLocation();
                    World world = event.getPlayer().getWorld();
                    Location from = reflexPlayer.getData().getFrom();
                    boolean ground = event.getPacket().getBooleans().getValues().get(0);

                    if (event.getPacketType().equals(PacketType.Play.Client.POSITION_LOOK)) {
                        double x = event.getPacket().getDoubles().getValues().get(0);
                        double y = event.getPacket().getDoubles().getValues().get(1);
                        double z = event.getPacket().getDoubles().getValues().get(2);
                        float yaw = event.getPacket().getFloat().getValues().get(0);
                        float pitch = event.getPacket().getFloat().getValues().get(1);

                        Location to = new Location(world, x, y, z, yaw, pitch);

                        if (from == null) {
                            reflexPlayer.getData().setFrom(to);
                            from = to;
                        }

                        ReflexAsyncMoveEvent moveEvent = new ReflexAsyncMoveEvent(event.getPlayer(), reflexPlayer, to, from, ground);
                        instance.getServer().getPluginManager().callEvent(moveEvent);

                        x = moveEvent.getTo().getX();
                        y = moveEvent.getTo().getY();
                        z = moveEvent.getTo().getZ();
                        yaw = moveEvent.getTo().getYaw();
                        pitch = moveEvent.getTo().getPitch();

                        event.getPacket().getDoubles().write(0, x);
                        event.getPacket().getDoubles().write(1, y);
                        event.getPacket().getDoubles().write(2, z);
                        event.getPacket().getFloat().write(0, yaw);
                        event.getPacket().getFloat().write(1, pitch);

                        reflexPlayer.getData().setFrom(to);
                    } else if (event.getPacketType().equals(PacketType.Play.Client.POSITION)) {
                        double x = event.getPacket().getDoubles().getValues().get(0);
                        double y = event.getPacket().getDoubles().getValues().get(1);
                        double z = event.getPacket().getDoubles().getValues().get(2);

                        Location to = new Location(world, x, y, z, location.getYaw(), location.getPitch());

                        if (from == null) {
                            reflexPlayer.getData().setFrom(to);
                            from = to;
                        }

                        ReflexAsyncMoveEvent moveEvent = new ReflexAsyncMoveEvent(event.getPlayer(), reflexPlayer, to, from, ground);
                        instance.getServer().getPluginManager().callEvent(moveEvent);

                        x = moveEvent.getTo().getX();
                        y = moveEvent.getTo().getY();
                        z = moveEvent.getTo().getZ();

                        event.getPacket().getDoubles().write(0, x);
                        event.getPacket().getDoubles().write(1, y);
                        event.getPacket().getDoubles().write(2, z);

                        reflexPlayer.getData().setFrom(to);
                    } else if (event.getPacketType().equals(PacketType.Play.Client.LOOK)) {
                        float yaw = event.getPacket().getFloat().getValues().get(0);
                        float pitch = event.getPacket().getFloat().getValues().get(1);

                        Location to = new Location(world, location.getX(), location.getY(), location.getZ(), yaw, pitch);

                        if (from == null) {
                            reflexPlayer.getData().setFrom(to);
                            from = to;
                        }

                        ReflexAsyncMoveEvent moveEvent = new ReflexAsyncMoveEvent(event.getPlayer(), reflexPlayer, to, from, ground);
                        instance.getServer().getPluginManager().callEvent(moveEvent);

                        yaw = moveEvent.getTo().getYaw();
                        pitch = moveEvent.getTo().getPitch();

                        event.getPacket().getFloat().write(0, yaw);
                        event.getPacket().getFloat().write(1, pitch);

                        reflexPlayer.getData().setFrom(to);
                    }
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
