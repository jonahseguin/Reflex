/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.base;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.util.obj.Lag;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflexTimer implements Runnable {

    private final Set<RTimer> timers = new HashSet<>();

    public ReflexTimer(Reflex instance) {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(instance, this, 20L, 20L);
    }

    public void registerTimer(RTimer timer) {
        timers.add(timer);
    }

    public void unregisterTimer(RTimer timer) {
        timers.remove(timer);
    }

    /**
     * Handles Timer Checks
     * They run once every second, this runnable is started in the main Reflex.java class
     *  ** ASYNC **
     */
    public void run() {
        for (RTimer timer : timers) {
            timer.runTimer();
        }
        for(Player pl : Bukkit.getOnlinePlayers()) {
            Reflex.getInstance().getCache().getReflexPlayer(pl).getData().setTps(Lag.getTPS());
        }
    }
}
