/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.movement;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:55.
 * Project: Reflex
 */
public class CheckFly extends Check {

    @ConfigData("minimum-hover-milliseconds")
    private long minHoverMS = 3000;

    @ConfigData("minimum-tps")
    private int minTps = 17;

    public CheckFly(Reflex reflex) {
        super(reflex, CheckType.FLY);

        reflex.getReflexScheduler().asyncRepeatingTask(() -> {
            for (ReflexPlayer reflexPlayer : Reflex.getReflexPlayers()) {
                Player player = reflexPlayer.getBukkitPlayer();
                if (player == null || !player.isOnline()) {
                    reflexPlayer.getData().lastVelocity = null;
                    reflexPlayer.getData().lastVelocityTime = 0;
                    continue;
                }
                if (reflexPlayer.getData().getLastVelocity() != null && reflexPlayer.getData().getLastVelocityTime() != 0) {
                    Vector velocity = reflexPlayer.getData().getLastVelocity();
                    long time = reflexPlayer.getData().getLastVelocityTime();
                    if (time + 500 > System.currentTimeMillis()) continue;
                    double velocityY = velocity.getY() * velocity.getY();
                    double y = player.getVelocity().getY() * player.getVelocity().getY();
                    if (y < 0.02) {
                        reflexPlayer.getData().lastVelocity = null;
                        reflexPlayer.getData().lastVelocityTime = 0;
                        continue;
                    }
                    if (y <= velocityY * 3.0) continue;
                    reflexPlayer.getData().lastVelocity = null;
                    reflexPlayer.getData().lastVelocityTime = 0;
                }
            }
        }, 0L, 1L);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMoveFlyHover(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer reflexPlayer = getPlayer(player);

        if (player.getAllowFlight()) return;
        if (reflexPlayer.getData().isInWater()) return;
        if (reflexPlayer.getData().isInWeb()) return;
        if (reflexPlayer.getData().blocksNear(player.getLocation())) {
            reflexPlayer.getData().flyHoverTime = 0;
            return;
        }
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) return;
        if (event.getTo().getY() != event.getFrom().getY()) {
            reflexPlayer.getData().flyHoverTime = 0;
            return;
        }

        if (reflexPlayer.getData().flyHoverTime == 0) {
            reflexPlayer.getData().flyHoverTime = System.currentTimeMillis();
        } else {
            if ((System.currentTimeMillis() - reflexPlayer.getData().flyHoverTime) >= minHoverMS) {
                if (Lag.getTPS() >= minTps) {
                    int seconds = Math.round((System.currentTimeMillis() - reflexPlayer.getData().flyHoverTime) / 1000);
                    fail(reflexPlayer, "Hover (" + seconds + "s)").cancelIfAllowed(event);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMoveFlyAscend(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer reflexPlayer = getPlayer(player);

        if (event.getTo().getY() > event.getFrom().getY()) {
            if (canCheckAscend(reflexPlayer)) {
                if (reflexPlayer.getData().lastVelocity == null && reflexPlayer.getData().lastVelocityTime == 0) {
                    long time = System.currentTimeMillis();
                    double blocks = 0.0;
                    if (reflexPlayer.getData().flyAscendBlocks != 0 && reflexPlayer.getData().flyAscendTime != 0) {
                        time = reflexPlayer.getData().flyAscendTime;
                        blocks = reflexPlayer.getData().flyAscendBlocks;
                    }
                    long millisDiff = System.currentTimeMillis() - time;
                    double offsetY = reflexPlayer.getData().offset(reflexPlayer.getData().getVerticalVector(event.getFrom().toVector()),
                            reflexPlayer.getData().getVerticalVector(event.getTo().toVector()));
                    if (offsetY > 0) {
                        blocks += offsetY;
                    }
                    if (reflexPlayer.getData().blocksNear(player.getLocation()) || reflexPlayer.getData().blocksNear(player.getLocation().subtract(0, 1, 0))) {
                        blocks = 0;
                    }
                    double limit = 0.5;
                    if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            if (effect.getType().equals(PotionEffectType.JUMP)) {
                                int level = effect.getAmplifier() + 1;
                                limit += Math.pow(level + 4.2, 2.0) / 16.0;
                                break;
                            }
                        }
                    }
                    if (blocks > limit) {
                        if (millisDiff > 500) {
                            fail(reflexPlayer, "Ascended " + Math.round(blocks) + " blocks").cancelIfAllowed(event);
                            time = System.currentTimeMillis();
                        }
                    } else {
                        time = System.currentTimeMillis();
                    }
                    reflexPlayer.getData().flyAscendTime = time;
                    reflexPlayer.getData().flyAscendBlocks = blocks;
                }
            }
        }
    }

    private boolean canCheckAscend(ReflexPlayer reflexPlayer) {
        Player player = reflexPlayer.getBukkitPlayer();
        return !player.getAllowFlight() && !player.isInsideVehicle() && player.getVehicle() == null;
    }


}
