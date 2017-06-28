/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;

import java.util.HashSet;
import java.util.Set;

/**
 * The ReflexTimer automatically calls a method in any registered oldchecks that implements RTimer, once a second.
 * Async.
 * <p>
 * Also updates the Ping and Lag variables within all player's data
 */
public class ReflexTimer implements Runnable {

    private final Set<RTimer> timers = new HashSet<>();
    private final Reflex instance;

    public ReflexTimer(Reflex instance) {
        this.instance = instance;
        instance.getServer().getScheduler().runTaskTimerAsynchronously(instance, this, 20L, 20L);
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
     * ** ASYNC **
     */
    public void run() {
        //Update ping and lag before doing timer checks
        for (RTimer timer : timers) {
            if (timer instanceof Check) {
                Check check = (Check) timer;
                if (check.isEnabled()) {
                    timer.runTimer();
                }
            } else {
                timer.runTimer();
            }
        }
    }
}
