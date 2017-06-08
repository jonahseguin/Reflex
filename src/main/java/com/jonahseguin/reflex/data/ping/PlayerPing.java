/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.data.ping;

import com.jonahseguin.reflex.Reflex;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Jonah Seguin on Sat 2017-05-27 at 12:49.
 * Project: Reflex
 */
public class PlayerPing {

    private final PingHandler pingHandler;
    private final ConcurrentMap<TickRange, PingTicks> pings = new ConcurrentHashMap<>();

    public PlayerPing() {
        this.pingHandler = Reflex.getInstance().getPingHandler();
    }

    public void setPing(int totalTick, long millisecond, int currentTick, int ping) {
        TickRange tickRange = getTickRange(totalTick);
        if (tickRange == null) {
            tickRange = createTickRange(totalTick, millisecond);
            pings.put(tickRange, new PingTicks());
        }
        pings.get(tickRange).tickPing(currentTick, ping);
    }

    public int getAveragePing() {
        PingHandler pingHandler = Reflex.getInstance().getPingHandler();
        return getAveragePing(pingHandler.getTotalTicks(), pingHandler.currentMillisecond());
    }

    public int getAveragePing(int totalTick, long currentMillisecond) {
        TickRange tickRange = getTickRangeOrCreate(totalTick, currentMillisecond);
        PingTicks pingTicks = getPingTicks(tickRange);
        return pingTicks.average();
    }

    public PingTicks getPingTicks(TickRange range) {
        return pings.get(range);
    }

    public TickRange getTickRangeOrCreate(int totalTick, long currentMillisecond) {
        TickRange range = getTickRange(totalTick);
        if (range == null) {
            range = createTickRange(totalTick, currentMillisecond);
            pings.put(range, new PingTicks());
        }
        return range;
    }

    public TickRange getTickRange(int totalTick) {
        for (TickRange tickRange : pings.keySet()) {
            if (tickRange.inRange(totalTick)) {
                return tickRange;
            }
        }
        return null;
    }

    public TickRange getTickRange(long millisecond) {
        for (TickRange tickRange : pings.keySet()) {
            if (tickRange.inRange(millisecond)) {
                return tickRange;
            }
        }
        return null;
    }

    public Set<TickRange> getTicksInMaxRange(long millisecond) {
        Set<TickRange> ranges = new HashSet<>();
        for (TickRange tickRange : pings.keySet()) {
            if (tickRange.inMaxRange(millisecond)) {
                ranges.add(tickRange);
            }
        }
        return ranges;
    }

    public TickRange createTickRange(int totalTick, long millisecond) {
        return new TickRange(totalTick, millisecond);
    }

    public void removeOldPings() {
        long currentMillisecond = Reflex.getInstance().getPingHandler().currentMillisecond(); // milliseconds since startup
        if (currentMillisecond >= (5 * 60 * 1000)) { // Keep 5 mins worth of pings
            long millisecond = currentMillisecond - (5 * 60 * 1000);
            Set<TickRange> ranges = getTicksInMaxRange(millisecond);
            Iterator<TickRange> it = ranges.iterator();
            while (it.hasNext()) {
                TickRange range = it.next();
                pings.remove(range);
            }
        }
    }

    public int pingSpikeDifference(int secondsAgo) {
        int currentPingAverage = getAveragePing();
        int beforePingAverage = getAveragePing(roundTick(pingHandler.getCurrentTick() - (secondsAgo * 20)),
                roundMillisecond(pingHandler.currentMillisecond() - (secondsAgo * 1000)));
        return Math.abs(beforePingAverage - currentPingAverage);
    }

    public boolean hasPingSpiked() {
        int difference = pingSpikeDifference(5);
        return difference > 75;
    }

    private int roundTick(int tick) {
        if (tick < 0) {
            tick = 0;
        }
        return tick;
    }

    private long roundMillisecond(long millisecond) {
        if (millisecond < 0) {
            millisecond = 0;
        }
        return millisecond;
    }

    public int getTotalTick(int secondsAgo) {
        return pingHandler.getTotalTicks() - (secondsAgo * 20);
    }

    public long getMillisecond(int secondsAgo) {
        return pingHandler.currentMillisecond() - (secondsAgo * 1000);
    }

    public int getAveragePing(int secondsAgo) {
        int totalTick = getTotalTick(secondsAgo);
        long millisecond = getMillisecond(secondsAgo);
        return getAveragePing(totalTick, millisecond);
    }


}
