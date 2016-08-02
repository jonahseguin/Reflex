/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.RTrigger;

public class CheckTriggerbot extends RTrigger implements RTimer {

    @ConfigData("cps-minimum-difference")
    private double cpsMinDifference = 7;

    @ConfigData("minimum-accuracy")
    private double minimumAccuracy = 0.6;

    public CheckTriggerbot() {
        super(CheckType.TRIGGER_BOT, RCheckType.TRIGGER);
    }

    //cps on target, cps off target : check every 15s

    //TODO: use similar check to one in accuracy to add CPS to player if hit/miss

    @Override
    public void runTimer() {
        Reflex.getReflexPlayers().forEach(rp -> {
            if (rp.getData().getTrigLastCheck() == -1L || ((System.currentTimeMillis() - rp.getData().getTrigLastCheck()) / 1000 >= 15)) {

                double cpsOn = rp.getData().getCpsOn();
                double cpsOff = rp.getData().getCpsOff();

                if (cpsOn > cpsOff) {
                    double accuracy = calculateAccuracy(rp.getData().getHits(), rp.getData().getMisses());
                    if (accuracy >= minimumAccuracy) {
                        if (cpsOn - cpsOff >= cpsMinDifference) {
                            fail(rp, Math.round(accuracy * 100) + "% accuracy");
                        }
                    }
                }

                rp.getData().setTrigLastCheck(System.currentTimeMillis());
            }
        });
    }

    public double calculateAccuracy(int hits, int misses) {
        if (hits == 0 && misses == 0) {
            return 0.0D;
        }
        else if (hits == 0) {
            return 0.0D;
        }
        else if (misses == 0) {
            return 1.0D;
        }
        return hits / misses;
    }

    @Override
    public int getCaptureTime() {
        return 0;
    }
}
