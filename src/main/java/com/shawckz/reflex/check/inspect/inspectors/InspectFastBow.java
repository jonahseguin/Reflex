/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.inspect.inspectors;

import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.CheckData;
import com.shawckz.reflex.check.data.PlayerData;
import com.shawckz.reflex.check.inspect.RInspect;
import com.shawckz.reflex.check.inspect.RInspectResultType;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InspectFastBow extends RInspect {

    @ConfigData("tps.ignore")
    private int tpsIgnore = 12;//TPS <= tpsIgnore - ignore the check

    @ConfigData("ping.ignore")
    private int pingIgnore = 130;//Ping >= pingIgnore - ignores the check

    @ConfigData("autoban-vl")
    private int autobanVL = 3;

    @ConfigData("ping.ideal-ping")
    private int idealPing = 60;

    @ConfigData("tps.ideal-tps")
    private int idealTps = 18;

    @ConfigData("ping.difference-threshold")
    private int pingThreshold = 60;

    @ConfigData("tps.difference-threshold")
    private int tpsThreshold = 2;

    public InspectFastBow() {
        super(CheckType.FAST_BOW, RCheckType.INSPECT);
    }

    @Override
    public RInspectResultType inspect(ReflexPlayer player, CheckData checkData, int dataPeriod) {
        if (checkData instanceof PlayerData) {
            PlayerData data = (PlayerData) checkData;

            if (data.getTps() > tpsIgnore) {
                if (data.getPing() < pingIgnore) {
                    if (data.getPing() > idealPing && data.getTps() < idealTps) {
                        double pingOffset = difference(data.getPing(), idealPing);
                        double tpsOffset = difference(data.getTps(), idealTps);
                        if (pingOffset <= pingThreshold && tpsOffset <= tpsThreshold) {
                            return RInspectResultType.FAILED;
                        }
                    }
                    else{
                        //They have ideal ping and tps
                        return RInspectResultType.FAILED;
                    }
                }
            }
            return RInspectResultType.PASSED;
        }
        else {
            throw new ReflexException("Cannot inspect data (CheckData type != inspect type)");
        }
    }

    private double difference(double a, double b) {
        return Math.max(a, b) - Math.min(a, b);
    }

}
