/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex;

import com.google.common.collect.ImmutableList;
import com.shawckz.reflex.check.Check;
import com.shawckz.reflex.check.CheckManager;
import com.shawckz.reflex.check.TimerCheck;
import com.shawckz.reflex.player.ReflexCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReflexTimer implements Runnable {
    private final List<TimerCheck> checks;

    public ReflexTimer(List<TimerCheck> checks) {
        this.checks = ImmutableList.copyOf(checks);
    }
    public ReflexTimer() {
        List<TimerCheck> list = new ArrayList<>();
        for (Check check : CheckManager.get().getChecks().values()) {
            if (check instanceof TimerCheck) {
                list.add((TimerCheck) check);
            }
        }
        this.checks = ImmutableList.copyOf(list);
    }

    /**
     * Handles Timer Checks
     * They run once every second, this runnable is started in the main Reflex.java class
     * .runTaskTimer - non async
     */
    public void run(){
        for (TimerCheck timerCheck : checks) {
            if(!timerCheck.isEnabled()) continue;
            for(Player pl : Bukkit.getOnlinePlayers()){
                if(ReflexCache.get().contains(pl.getName())){
                    timerCheck.check(ReflexCache.get().getAresPlayer(pl.getName()));
                }
            }
        }
    }
}
