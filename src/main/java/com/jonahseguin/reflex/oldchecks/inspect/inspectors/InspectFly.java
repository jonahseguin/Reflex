/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.inspect.inspectors;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.oldchecks.inspect.RInspect;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultData;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultType;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.data.CheckData;
import com.jonahseguin.reflex.oldchecks.data.checkdata.DataFly;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
public class InspectFly extends RInspect {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @ConfigData("ban-vl")
    private int autobanVL = 3;

    @ConfigData("threshold-airtime")
    private int thresholdAirtime = 5;

    @ConfigData("threshold-yps")
    private double thresholdYps = 10;

    @ConfigData("threshold-bps")
    private double thresholdBps = 28;

    public InspectFly(Reflex instance) {
        super(instance, CheckType.FLY, RCheckType.INSPECT);
    }

    @Override
    public RInspectResultData inspect(ReflexPlayer player, CheckData checkData, int dataPeriod) {
        if (checkData instanceof DataFly) {
            DataFly data = (DataFly) checkData;

            if (data.getPeakAirTime() != -1) {
                if (data.getPeakAirTime() >= thresholdAirtime) {
                    return new RInspectResultData(RInspectResultType.FAILED, df.format(data.getPeakAirTime()) + "s in air");
                }
            }
            else if (data.getPeakYps() >= thresholdYps) {
                return new RInspectResultData(RInspectResultType.FAILED, df.format(data.getPeakYps()) + " peak y/sec");
            }
            else if (data.getPeakBps() >= thresholdBps) {
                return new RInspectResultData(RInspectResultType.FAILED, df.format(data.getPeakBps()) + " peak blocks/sec");
            }

            return new RInspectResultData(RInspectResultType.PASSED);

        }
        else {
            throw new ReflexException("Cannot inspect data (CheckData type != inspect type)");
        }
    }

    private double difference(double a, double b) {
        return Math.max(a, b) - Math.min(a, b);
    }

}
