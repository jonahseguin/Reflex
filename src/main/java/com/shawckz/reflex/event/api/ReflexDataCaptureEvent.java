/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.player.reflex.ReflexPlayer;

public class ReflexDataCaptureEvent extends ReflexAPIEvent {

    private final RDataCapture dataCapture;
    private final ReflexPlayer player;

    public ReflexDataCaptureEvent(RDataCapture dataCapture, ReflexPlayer player) {
        this.dataCapture = dataCapture;
        this.player = player;
    }

    public RDataCapture getDataCapture() {
        return dataCapture;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }
}
