/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.data.capturers.CaptureAutoClick;
import com.shawckz.reflex.check.data.capturers.CaptureVClip;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.utility.ReflexCaller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataCaptureManager {

    private ConcurrentMap<CheckType, RDataCapture> dataCaptures = new ConcurrentHashMap<>();

    public DataCaptureManager(Reflex instance) {

    }

    public void setup() {
        //TODO: Register

        register(new CaptureAutoClick());
        register(new CaptureVClip());

        dataCaptures.values().stream().forEach(RDataCapture::setupConfig);
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
        CaptureTask task = new CaptureTask(player, checkType, captureTime) {
            @Override
            public void onFinish(CheckData data) {
                callback.call(data);
            }
        };
        task.start();
    }

}
