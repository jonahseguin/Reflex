/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.inspect.inspectors;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.data.CheckData;
import com.jonahseguin.reflex.oldchecks.data.checkdata.DataRegen;
import com.jonahseguin.reflex.oldchecks.inspect.RInspect;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultData;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InspectRegen extends RInspect {

    @ConfigData("max-health-per-second")
    private int maxHps = 2;

    @ConfigData("hps-threshold")
    private int threshold = 8;

    @ConfigData("tps.ignore")
    private int tpsIgnore = 12;//TPS <= tpsIgnore - ignore the oldchecks

    @ConfigData("ping.ignore")
    private int pingIgnore = 130;//Ping >= pingIgnore - ignores the oldchecks

    @ConfigData("ban-vl")
    private int autobanVL = 3;

    @ConfigData("ping.ideal-ping")
    private int idealPing = 60;

    @ConfigData("tps.ideal-tps")
    private int idealTps = 18;

    @ConfigData("ping.difference-threshold")
    private int pingThreshold = 60;

    @ConfigData("tps.difference-threshold")
    private int tpsThreshold = 2;

    public InspectRegen(Reflex instance) {
        super(instance, CheckType.REGEN, RCheckType.INSPECT);
    }

    @Override
    public RInspectResultData inspect(ReflexPlayer player, CheckData checkData, int dataPeriod) {
        if (checkData instanceof DataRegen) {
            DataRegen data = (DataRegen) checkData;

            double peakHps = data.getHps();

            if (data.getTps() > tpsIgnore) {
                if (data.getPing() < pingIgnore) {
                    if (data.getPing() > idealPing && data.getTps() < idealTps) {
                        double pingOffset = difference(data.getPing(), idealPing);
                        double tpsOffset = difference(data.getTps(), idealTps);
                        if (pingOffset <= pingThreshold && tpsOffset <= tpsThreshold) {
                            if (peakHps >= maxHps) {
                                return new RInspectResultData(RInspectResultType.FAILED, "peak " + peakHps + " hps");
                            }
                        }
                    }
                    else {
                        if (peakHps >= maxHps) {
                            return new RInspectResultData(RInspectResultType.FAILED, "peak " + peakHps + " hps");
                        }
                    }
                }
            }

            if (peakHps >= threshold) {
                return new RInspectResultData(RInspectResultType.FAILED, "peak " + peakHps + " hps");
            }

            return new RInspectResultData(RInspectResultType.PASSED, "peak " + peakHps + " hps");
        }
        else {
            throw new ReflexException("Cannot inspect data (CheckData type != inspect type)");
        }
    }

    private double difference(double a, double b) {
        return Math.max(a, b) - Math.min(a, b);
    }

}
