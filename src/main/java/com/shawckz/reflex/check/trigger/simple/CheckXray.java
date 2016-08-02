/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.backend.configuration.annotations.ConfigSerializer;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.XrayStats;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.serial.XrayStatsMaxSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
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
    private Map<XrayStats.Stat, Double> max = new HashMap<>();


    public CheckXray() {
        super(CheckType.XRAY, RCheckType.TRIGGER);

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
        }.runTaskTimerAsynchronously(Reflex.getInstance(), 72000, 72000);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
        if (!isEnabled()) return;

        if (!e.getBlock().hasMetadata("ReflexXrayPlaced")) {
            XrayStats xrayStats = rp.getData().getXrayStats();
            XrayStats.Stat stat = xrayStats.convertStat(e.getBlock().getType());
            double val = xrayStats.modifyStat(stat, 1);
            if (xrayStats.overMax(stat)) {
                if (fail(rp, val + " " + stat.toString().toLowerCase() + "/hr").isCancelled()) {
                    e.setCancelled(true);
                    xrayStats.modifyStat(stat, ((-1) * violationPenalty));
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
        if (rp.getData().getXrayStats().isTracking(e.getBlock().getType())) {
            e.getBlock().setMetadata("ReflexXrayPlaced", new FixedMetadataValue(Reflex.getInstance(), true));
        }
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }
}
