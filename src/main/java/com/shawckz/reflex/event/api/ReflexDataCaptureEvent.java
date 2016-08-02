/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.player.reflex.ReflexPlayer;

import org.bukkit.event.Cancellable;

public class ReflexDataCaptureEvent extends ReflexAPIEvent implements Cancellable {

    private final CheckType checkType;
    private final RDataCapture dataCapture;
    private final ReflexPlayer player;
    private int captureTime;

    private boolean cancelled = false;

    public ReflexDataCaptureEvent(CheckType checkType, RDataCapture dataCapture, ReflexPlayer player, int captureTime) {
        this.checkType = checkType;
        this.dataCapture = dataCapture;
        this.player = player;
        this.captureTime = captureTime;
    }

    public RDataCapture getDataCapture() {
        return dataCapture;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public int getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(int captureTime) {
        this.captureTime = captureTime;
    }
}
