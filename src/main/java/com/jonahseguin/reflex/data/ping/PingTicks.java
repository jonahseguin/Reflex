/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.data.ping;

import net.minecraft.util.io.netty.util.internal.ConcurrentSet;
import org.apache.commons.lang.Validate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Created by Jonah Seguin on Sat 2017-05-27 at 12:49.
 * Project: Reflex
 */
public class PingTicks {

    private ConcurrentMap<Integer, Integer> pingPerTick = new ConcurrentHashMap<>();

    public int getPing(int tick) {
        return pingPerTick.getOrDefault(tick, -1);
    }

    public void tickPing(int tick, int ping) {
        pingPerTick.put(tick, ping);
    }

    public int average(int... ticks) {
        Validate.notNull(ticks);
        ConcurrentSet<Integer> pings;
        if (ticks.length > 0) {
            pings = new ConcurrentSet<>();
            for (int tick : ticks) {
                pings.add(getPing(tick));
            }
        } else {
            //All ticks
            pings = new ConcurrentSet<>();
            pings.addAll(pingPerTick.values());
        }
        int count = pings.size();
        if (count == 0) {
            return 0;
        }
        int totalPing = 0;
        for (int ping : pings) {
            totalPing += ping;
        }
        return Math.round(totalPing / count);
    }

    public int ticks() {
        return pingPerTick.keySet().size();
    }

    public boolean isFull() {
        return ticks() >= 20;
    }

}
