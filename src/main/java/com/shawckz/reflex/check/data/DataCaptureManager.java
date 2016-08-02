/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.data.capturers.*;
import com.shawckz.reflex.event.api.ReflexDataCaptureEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.utility.ReflexCaller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;

public class DataCaptureManager {

    private ConcurrentMap<CheckType, RDataCapture> dataCaptures = new ConcurrentHashMap<>();

    public DataCaptureManager(Reflex instance) {

    }

    public void setup() {
        //TODO: Register

        register(new CaptureAutoClick());
        register(new CaptureVClip());
        register(new CaptureRegen());
        register(new CaptureReach());
        register(new CaptureFly());

        dataCaptures.values().stream().forEach(RDataCapture::setupConfig);
    }

    public ConcurrentMap<CheckType, RDataCapture> getDataCaptures() {
        return dataCaptures;
    }

    public RDataCapture getDataCapture(CheckType checkType) {
        return dataCaptures.get(checkType);
    }

    public void register(RDataCapture dataCapture) {
        dataCaptures.put(dataCapture.getCheckType(), dataCapture);
        dataCapture.setEnabled(dataCapture.isEnabled());
        if (dataCapture instanceof RTimer) {
            RTimer timer = (RTimer) dataCapture;
            Reflex.getInstance().getReflexTimer().registerTimer(timer);
        }
    }

    public void startCaptureTask(ReflexPlayer player, CheckType checkType, int captureTime, final ReflexCaller<CheckData> callback) {
        ReflexDataCaptureEvent dataCaptureEvent = new ReflexDataCaptureEvent(checkType, getDataCapture(checkType), player, captureTime);
        Bukkit.getServer().getPluginManager().callEvent(dataCaptureEvent);
        if (!dataCaptureEvent.isCancelled()) {
            CaptureTask task = new CaptureTask(player, checkType, dataCaptureEvent.getCaptureTime()) {
                @Override
                public void onFinish(CheckData data) {
                    callback.call(data);
                }
            };
            task.start();
        }
    }

}
