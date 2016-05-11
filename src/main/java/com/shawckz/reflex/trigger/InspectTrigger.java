/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.trigger;

import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.player.ReflexPlayer;
import lombok.Getter;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
public class InspectTrigger {

    private long start;
    private long expiry;
    private long period;
    private CheckType checkType;
    private ReflexPlayer player;

    public InspectTrigger(ReflexPlayer player, CheckType checkType, long period) {
        this.player = player;
        this.checkType = checkType;
        this.period = period;
        this.start = System.currentTimeMillis();
        this.expiry = (start + (1000 * period));
    }

    public boolean expired() {
        return System.currentTimeMillis() >= expiry;
    }

}
