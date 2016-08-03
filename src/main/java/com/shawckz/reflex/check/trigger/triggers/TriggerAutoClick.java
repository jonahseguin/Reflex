/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
@Setter
public class TriggerAutoClick extends RTrigger implements RTimer {

    @ConfigData(value = "consistent-clicks-per-second")
    private int maxClicksPerSecond = 15;

    @ConfigData(value = "cps-threshold")
    private int threshold = 24;

    @ConfigData("capture-time")
    private int captureTime = 15;

    public TriggerAutoClick(Reflex instance) {
        super(instance, CheckType.AUTO_CLICK, RCheckType.TRIGGER);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            ReflexPlayer p = getPlayer(pl);
            updateCps(p, 1);//Add 1 to their most recent CPS
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Player pl = e.getPlayer();
        ReflexPlayer p = getPlayer(pl);
        updateCps(p, -0.5);
    }

    private void updateCps(ReflexPlayer p, double i) {
        double[] cps = p.getData().getClicksPerSecond();
        cps[3] = cps[3] + i;
        p.getData().setClicksPerSecond(cps);
    }

    @Override
    public void runTimer() {
        Reflex.getReflexPlayers().forEach(player -> {
            double[] cps = player.getData().getClicksPerSecond();

            if (cps[3] >= maxClicksPerSecond
                    && cps[2] >= maxClicksPerSecond
                    && cps[1] >= maxClicksPerSecond
                    && cps[0] >= maxClicksPerSecond) {
                triggerLater(player, result -> {

                });
            }
            else if (cps[3] > threshold) {
                triggerLater(player, result -> {

                });
            }

            cps[0] = cps[1];
            cps[1] = cps[2];
            cps[2] = cps[3];
            cps[3] = 0.0;
            player.getData().setClicksPerSecond(cps);
        });
    }


}
