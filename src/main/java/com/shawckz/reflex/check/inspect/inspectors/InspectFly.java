/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.inspect.inspectors;

import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.CheckData;
import com.shawckz.reflex.check.data.checkdata.DataFly;
import com.shawckz.reflex.check.inspect.RInspect;
import com.shawckz.reflex.check.inspect.RInspectResultData;
import com.shawckz.reflex.check.inspect.RInspectResultType;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.utility.ReflexException;
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
    private int thresholdAirtime = 8;

    @ConfigData("threshold-yps")
    private double thresholdYps = 10;

    @ConfigData("threshold-bps")
    private double thresholdBps = 28;

    public InspectFly() {
        super(CheckType.FLY, RCheckType.INSPECT);
    }

    @Override
    public RInspectResultData inspect(ReflexPlayer player, CheckData checkData, int dataPeriod) {
        if (checkData instanceof DataFly) {
            DataFly data = (DataFly) checkData;

            if(data.getPeakAirTime() != -1) {
                if(data.getPeakAirTime() >= thresholdAirtime) {
                    return new RInspectResultData(RInspectResultType.FAILED, df.format(data.getPeakAirTime()) + "s in air");
                }
            }
            else if(data.getPeakYps() >= thresholdYps) {
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
