/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.inspect.inspectors;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.data.CheckData;
import com.jonahseguin.reflex.oldchecks.data.checkdata.DataReach;
import com.jonahseguin.reflex.oldchecks.inspect.RInspect;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultData;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class InspectReach extends RInspect {

    @ConfigData("ban-vl")
    private int autobanVL = 3;

    @ConfigData("ping-distance-multiplier")
    private double pingMultiplier = 1.20D;

    @ConfigData("max-ping-to-multiply")
    private int maxPingToMultiply = 60;

    @ConfigData("max-distance")
    private double maxDistance = 6;

    @ConfigData("distance-threshold")
    private double threshold = 8;

    public InspectReach(Reflex instance) {
        super(instance, CheckType.REACH, RCheckType.INSPECT);
    }

    @Override
    public RInspectResultData inspect(ReflexPlayer player, CheckData checkData, int dataPeriod) {
        if (checkData instanceof DataReach) {
            DataReach data = (DataReach) checkData;

            double peakRange = data.getPeakReach();

            for (Map.Entry<Double, Integer> entry : data.getEntries()) {
                double distance = entry.getKey();
                int ping = entry.getValue();

                double mD = maxDistance;

                if (ping <= maxPingToMultiply) {
                    mD *= pingMultiplier;
                }

                if (distance >= mD) {
                    return new RInspectResultData(RInspectResultType.FAILED, "peak range " + data.getPeakReach());
                }
            }

            if (peakRange >= threshold) {
                return new RInspectResultData(RInspectResultType.FAILED, "peak range " + data.getPeakReach());
            }


            return new RInspectResultData(RInspectResultType.PASSED, "peak range " + data.getPeakReach());
        } else {
            throw new ReflexException("Cannot inspect data (CheckData type != inspect type)");
        }
    }

    private double difference(double a, double b) {
        return Math.max(a, b) - Math.min(a, b);
    }

}
