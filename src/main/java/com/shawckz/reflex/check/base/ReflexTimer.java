/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.base;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Lag;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ReflexTimer implements Runnable {

    private final Set<RTimer> timers = new HashSet<>();

    public ReflexTimer(Reflex instance) {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(instance, this, 20L, 20L);
    }

    public void registerTimer(RTimer timer) {
        timers.add(timer);
    }

    public boolean hasTimer(RTimer timer) {
        return timers.contains(timer);
    }

    public void unregisterTimer(RTimer timer) {
        timers.remove(timer);
    }

    public void clear() {
        timers.clear();
    }

    /**
     * Handles Timer Checks
     * They run once every second, this runnable is started in the main Reflex.java class
     *  ** ASYNC **
     */
    public void run() {
        for (RTimer timer : timers) {
            if(timer instanceof Check) {
                Check check = (Check) timer;
                if(check.isEnabled()) {
                    timer.runTimer();
                }
            }
            else{
                timer.runTimer();
            }
        }
        for (Player pl : Bukkit.getOnlinePlayers()) {
            Reflex.getInstance().getCache().getReflexPlayer(pl).getData().setTps(Lag.getTPS());
            Reflex.getInstance().getCache().getReflexPlayer(pl).getData().setPing(((CraftPlayer) pl).getHandle().ping);
            for (RDataCapture check : Reflex.getInstance().getDataCaptureManager().getDataCaptures().values()) {
                updatePingAndLag(Reflex.getInstance().getCache().getReflexPlayer(pl), check);
            }
        }
    }

    private void updatePingAndLag(ReflexPlayer player, RDataCapture check) {
        if(player != null && player.getBukkitPlayer() != null) {
            if (check.isCapturing(player.getBukkitPlayer())) {
                player.getCapturePlayer().getData(check.getCheckType()).setPing(((CraftPlayer) player.getBukkitPlayer()).getHandle().ping);
                player.getCapturePlayer().getData(check.getCheckType()).setTps(Lag.getTPS());
            }
        }
    }

}
