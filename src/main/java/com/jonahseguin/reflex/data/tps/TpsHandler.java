/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.data.tps;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.data.ping.TickRange;
import com.jonahseguin.reflex.util.obj.Lag;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonah Seguin on Sun 2017-05-28 at 11:51.
 * Project: Reflex
 */
@Getter
public class TpsHandler {

    private Reflex reflex;
    private int currentTick = 1;
    private int totalTick = 0;
    private int totalCycles = 0;
    private double totalTps = 0;
    private Map<TickRange, Double> tpsRange = new HashMap<>();
    private long startMillisecond = 0;

    public TpsHandler(Reflex reflex) {
        this.reflex = reflex;
        this.startMillisecond = System.currentTimeMillis();
        new BukkitRunnable() {
            @Override
            public void run() {
                currentTick++;
                totalTick++;
                if (currentTick > 20) {
                    currentTick = 1;
                    totalCycles++;
                    double currentTps = Lag.getTPS();
                    totalTps += currentTps;
                    TickRange tickRange = new TickRange(currentTick, currentMillisecond());
                    tpsRange.put(tickRange, currentTps);
                }
            }
        }.runTaskTimerAsynchronously(reflex, 0L, 1L);
    }

    public long currentMillisecond() {
        return System.currentTimeMillis() - startMillisecond;
    }

    public double getCurrentTps() {
        TickRange tickRange = new TickRange(totalTick, currentMillisecond());
        if (tpsRange.containsKey(tickRange)) {
            return tpsRange.get(tickRange);
        } else {
            return Lag.getTPS();
        }
    }

    public double getTps(int secondsAgo) {
        int agoTotalTick = (totalTick - (secondsAgo * 20));
        for (TickRange tickRange : tpsRange.keySet()) {
            if (tickRange.inRange(agoTotalTick)) {
                return tpsRange.get(tickRange);
            }
        }
        return -1;
    }

    public boolean hasTpsSpiked(int secondsAgo) {
        double tps = getCurrentTps();
        double beforeTps = getTps(secondsAgo);
        double difference = Math.abs(tps - beforeTps);
        return difference > 3;
    }

}
