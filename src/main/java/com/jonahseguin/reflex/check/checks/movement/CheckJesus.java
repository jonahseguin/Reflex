/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.movement;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jonah Seguin on Tue 2017-05-09 at 20:23.
 * Project: Reflex
 */
public class CheckJesus extends Check {

    @ConfigData("minimum-attempts")
    private int minAttempts = 3;

    @ConfigData("minimum-hover-milliseconds")
    private long minHoverMs = 300;

    public CheckJesus(Reflex reflex) {
        super(reflex, CheckType.JESUS);
    }

    @EventHandler(ignoreCancelled = true)
    public void onJesusMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) return;
        if (player.getAllowFlight()) return;
        final ReflexPlayer rp = getPlayer(player);
        if (!(player.getNearbyEntities(1, 1, 1)).isEmpty()) return;


        if (rp.getData().cannotStandWater(player.getWorld().getBlockAt(player.getLocation()))
                && rp.getData().isAboveWater() && !rp.getData().isFullyInWater()) {
            if (rp.getData().jesusTime == 0) {
                rp.getData().jesusTime = System.currentTimeMillis();
            } else {
                long millis = System.currentTimeMillis() - rp.getData().jesusTime;
                if (millis >= minHoverMs) {
                    rp.addPreVL(getCheckType());
                    if (rp.getPreVL(getCheckType()) >= minAttempts) {
                        fail(rp, Math.round(millis) + "ms hovering").cancelIfAllowed(event);
                        rp.setPreVL(getCheckType(), 0);
                    }
                }
            }
        } else {
            rp.getData().jesusTime = 0;
        }
    }

}
