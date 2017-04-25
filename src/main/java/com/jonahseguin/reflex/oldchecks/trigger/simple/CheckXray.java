/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.simple;

import com.google.common.collect.Maps;
import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigSerializer;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.data.XrayStats;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.serial.XrayStatsMaxSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class CheckXray extends RTrigger {

    @ConfigData(value = "violation-subtract-stat-amount")
    private int violationPenalty = 5;

    @ConfigData("xray-stats-max")
    @ConfigSerializer(serializer = XrayStatsMaxSerializer.class)
    private Map<XrayStats.Stat, Double> max = Maps.newHashMap();


    public CheckXray(Reflex instance) {
        super(instance, CheckType.XRAY, RCheckType.TRIGGER);

        for (XrayStats.Stat stat : XrayStats.Stat.values()) {
            if (!max.containsKey(stat)) {
                max.put(stat, stat.max);
            }
        }
        save();

        new BukkitRunnable() {
            @Override
            public void run() {
                Reflex.getReflexPlayers().forEach(rp -> rp.getData().getXrayStats().reset());
            }
        }.runTaskTimerAsynchronously(instance, 72000, 72000);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = getPlayer(p);
        if (!isEnabled()) return;

        if (!e.getBlock().hasMetadata("ReflexXrayPlaced")) {
            XrayStats xrayStats = rp.getData().getXrayStats();
            XrayStats.Stat stat = xrayStats.convertStat(e.getBlock().getType());
            if (stat != null) {
                double val = xrayStats.modifyStat(stat, 1);
                if (xrayStats.overMax(stat)) {
                    if (fail(rp, val + " " + stat.toString().toLowerCase() + "/hr").isCancelled()) {
                        e.setCancelled(true);
                        xrayStats.modifyStat(stat, ((-1) * violationPenalty));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = getPlayer(p);
        if (rp.getData().getXrayStats().isTracking(e.getBlock().getType())) {
            e.getBlock().setMetadata("ReflexXrayPlaced", new FixedMetadataValue(getReflex(), true));
        }
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }
}
