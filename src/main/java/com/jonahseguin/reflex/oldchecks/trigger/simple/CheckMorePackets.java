/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.simple;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.base.RTimer;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class CheckMorePackets extends RTrigger implements RTimer {

    @ConfigData("max-packets-per-second")
    private double maxPPS = 25; //Maximum packets per-second per-player (1 packet per tick) + 5 (lag safety)

    @ConfigData("trigger-threshold")
    private int trigger = 3;

    public CheckMorePackets(Reflex instance) {
        super(instance, CheckType.MORE_PACKETS, RCheckType.TRIGGER);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = getPlayer(p);
        rp.getData().setPackets(rp.getData().getPackets() + 1);
    }

    @Override
    public void runTimer() {
        final double newMaxPPS = calcMaxPPS();
        Reflex.getReflexPlayers().forEach(rp -> {
            if (rp.getData().getPackets() > newMaxPPS) {
                rp.addAlertVL(getCheckType());
                if (rp.getAlertVL(getCheckType()) >= trigger) {
                    fail(rp, rp.getData().getPackets() + " packets");
                    rp.setAlertVL(getCheckType(), 0);
                }
            }

            rp.getData().setPackets(0);
        });
    }

    private double calcMaxPPS() {
        return maxPPS * 20 / Lag.getTPS();
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }
}
