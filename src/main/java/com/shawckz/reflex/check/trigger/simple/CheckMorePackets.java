/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.event.internal.ReflexAsyncMoveEvent;
import com.shawckz.reflex.event.internal.ReflexFlyingEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Lag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CheckMorePackets extends RTrigger implements RTimer {

    private final double maxPPS = 30; //Maximum packets per-second per-player (1 packet per tick) + 10 for safety

    @ConfigData("trigger-threshold")
    private int trigger = 3;

    public CheckMorePackets() {
        super(CheckType.MORE_PACKETS, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onAsyncMove(ReflexAsyncMoveEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);

        rp.getData().setPackets(rp.getData().getPackets() + 1);
    }

    @EventHandler
    public void onFlying(ReflexFlyingEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);

        rp.getData().setPackets(rp.getData().getPackets() + 1);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(e.isCancelled()) {
            Player p = e.getPlayer();
            ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
            rp.getData().setPackets(rp.getData().getPackets() - 2);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
        rp.getData().setPackets(rp.getData().getPackets() - 2);
    }

    @Override
    public void runTimer() {
        for(Player pl : Bukkit.getOnlinePlayers()) {
            ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(pl);

            if(rp.getData().getPackets() > calcMaxPPS()) {
                rp.addAlertVL(getCheckType());
                if(rp.getAlertVL(getCheckType()) >= trigger) {
                    fail(rp, rp.getData().getPackets() + " packets");
                    rp.setAlertVL(getCheckType(), 0);
                }
            }

            rp.getData().setPackets(0);
        }
    }

    private double calcMaxPPS() {
        return maxPPS * 20 / Lag.getTPS();
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }
}
