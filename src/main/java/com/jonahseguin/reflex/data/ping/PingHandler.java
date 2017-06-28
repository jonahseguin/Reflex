/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.data.ping;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah Seguin on Sat 2017-05-27 at 12:45.
 * Project: Reflex
 */
@Getter
public class PingHandler {

    private Reflex reflex;
    private int totalTicks = 0;
    private int currentTick = 1;
    private int tickCycles = 0; // Every second
    private long startMillisecond = 0;

    public PingHandler(Reflex reflex) {
        this.reflex = reflex;
        this.startMillisecond = System.currentTimeMillis();
        new BukkitRunnable() {
            @Override
            public void run() {
                totalTicks++;
                boolean reset = updateCurrentTick();
                for (ReflexPlayer reflexPlayer : Reflex.getReflexPlayers()) {
                    PlayerPing playerPing = reflexPlayer.getPlayerPing();
                    int ping = reflexPlayer.getPing();
                    playerPing.setPing(totalTicks, currentMillisecond(), currentTick, ping);
                    if (reset) {
                        playerPing.removeOldPings();
                    }
                }
            }
        }.runTaskTimerAsynchronously(reflex, 0L, 1L);
    }

    private boolean updateCurrentTick() {
        boolean reset = false;
        currentTick++;
        if (currentTick > 20) {
            currentTick = 1;
            tickCycles++;
            reset = true;
        }
        return reset;
    }

    public long currentMillisecond() {
        return System.currentTimeMillis() - startMillisecond;
    }

}
