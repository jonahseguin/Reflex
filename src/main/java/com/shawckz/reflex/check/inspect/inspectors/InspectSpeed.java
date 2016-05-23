/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.inspect.inspectors;

import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.CheckData;
import com.shawckz.reflex.check.data.checkdata.DataSpeed;
import com.shawckz.reflex.check.inspect.RInspect;
import com.shawckz.reflex.check.inspect.RInspectResultData;
import com.shawckz.reflex.check.inspect.RInspectResultType;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@Setter
public class InspectSpeed extends RInspect {

    @ConfigData("ban-vl")
    private int autobanVL = 3;

    @ConfigData("max-blocks-per-second")
    private double maxBlocksPerSecond = 7.5;

    @ConfigData("bps-threshold")
    private double threshold = 25;

    public InspectSpeed() {
        super(CheckType.SPEED, RCheckType.INSPECT);
    }

    @Override
    public RInspectResultData inspect(ReflexPlayer player, CheckData checkData, int dataPeriod) {
        if (checkData instanceof DataSpeed) {
            DataSpeed data = (DataSpeed) checkData;

            double maxDistance = maxBlocksPerSecond * dataPeriod;

            double peakBps = data.getPeakBlocksPerSecond();

            double totalDistance = 0;

            for (DataSpeed.SpeedData sd : data.getData()) {
                PotionEffect effect = null;
                for (PotionEffect e : sd.getPotionEffects()) {
                    if (e.getType() == PotionEffectType.SPEED) {
                        effect = e;
                        break;
                    }
                }
                double distance = sd.getDistance();
                if (effect != null) {
                    distance -= (distance) * (effect.getAmplifier() * 0.2);
                }
                totalDistance += distance;
            }

            double bps = totalDistance / dataPeriod;

            if (totalDistance > maxDistance) {
                return new RInspectResultData(RInspectResultType.FAILED, Math.round(peakBps) + " peak blocks/s");
            }

            if (peakBps > threshold) {
                return new RInspectResultData(RInspectResultType.FAILED, Math.round(peakBps) + " peak blocks/s");
            }

            return new RInspectResultData(RInspectResultType.PASSED, Math.round(peakBps) + " peak blocks/s");
        }
        else {
            throw new ReflexException("Cannot inspect data (CheckData type != inspect type)");
        }
    }

    private double difference(double a, double b) {
        return Math.max(a, b) - Math.min(a, b);
    }

}
