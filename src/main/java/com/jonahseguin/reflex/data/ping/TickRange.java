/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.data.ping;

/**
 * Created by Jonah Seguin on Sat 2017-05-27 at 13:01.
 * Project: Reflex
 */
public class TickRange {

    private int minTick;
    private int maxTick;
    private long minMillisecond;
    private long maxMillisecond;

    public TickRange(int totalTick, long millisecond) {
        this(totalTick, totalTick + 20, millisecond, millisecond + 1000);
    }

    public TickRange(int minTick, int maxTick, long minMillisecond, long maxMillisecond) {
        this.minTick = minTick;
        this.maxTick = maxTick;
        this.minMillisecond = minMillisecond;
        this.maxMillisecond = maxMillisecond;
    }

    public boolean inRange(int totalTick) {
        return totalTick <= maxTick && totalTick >= minTick;
    }

    public boolean inRange(long millisecond) {
        return millisecond <= maxMillisecond && millisecond >= minMillisecond;
    }

    public boolean inMaxRange(long millisecond) {
        return millisecond <= maxMillisecond;
    }

}
