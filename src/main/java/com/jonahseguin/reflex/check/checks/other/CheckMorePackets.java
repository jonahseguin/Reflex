/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.other;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.check.RTimer;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:56.
 * Project: Reflex
 * <p>
 * Movement packets --> max 1 per tick (20 per second)
 * Checks if too many are being sent (can catch certain speeds, auras, etc.)
 */
public class CheckMorePackets extends Check implements RTimer {

    @ConfigData("max-packets-per-second")
    private double maxPPS = 30; //Maximum packets per-second per-player (1 packet per tick) + 10 (lag safety)

    @ConfigData("minimum-attempts")
    private int minAttempts = 3;

    public CheckMorePackets(Reflex instance) {
        super(instance, CheckType.MORE_PACKETS);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = getPlayer(p);
        rp.getData().packets++;
    }

    @Override
    public void runTimer() {
        final double maxPPS = calcMaxPPS();
        Reflex.getReflexPlayers().forEach(rp -> {
            if (rp.getData().packets > maxPPS) {
                rp.addPreVL(getCheckType());
                if (rp.getPreVL(getCheckType()) >= minAttempts) {
                    fail(rp, rp.getData().packets + " packets");
                    rp.setPreVL(getCheckType(), 0);
                }
            } else {
                rp.setPreVL(getCheckType(), 0); // Reset if they don't violate 3 in a row
            }
            rp.getData().packets = 0;
        });
    }

    private double calcMaxPPS() {
        return maxPPS * 20 / Lag.getTPS();
    }

}
