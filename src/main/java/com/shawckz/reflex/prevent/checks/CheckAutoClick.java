/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.prevent.checks;

import com.shawckz.reflex.core.configuration.annotations.ConfigData;
import com.shawckz.reflex.core.player.ReflexCache;
import com.shawckz.reflex.core.player.ReflexPlayer;
import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.prevent.check.TimerCheck;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CheckAutoClick extends TimerCheck {

    @ConfigData(value = "max-clicks-per-second")
    @Getter @Setter private int maxClicksPerSecond = 20;

    public CheckAutoClick() {
        super(CheckType.AUTO_CLICK);
    }

    @Override
    public void check(ReflexPlayer player) {
        double[] cps = player.getData().getClicksPerSecond();

        if(cps[3] >= maxClicksPerSecond
                && cps[2] >= maxClicksPerSecond
                && cps[1] >= maxClicksPerSecond
                && cps[0] >= maxClicksPerSecond){
            fail(player);
        }

        cps[0] = cps[1];
        cps[1] = cps[2];
        cps[2] = cps[3];
        cps[3] = 0.0;
        player.getData().setClicksPerSecond(cps);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent e){
        Player pl = e.getPlayer();
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
            updateCps(p,1);//Add 1 to their most recent CPS
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e){
        if(e.isCancelled()) return;
        Player pl = e.getPlayer();
        ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
        updateCps(p, -0.5);
    }

    private void updateCps(ReflexPlayer p, double i){
        double[] cps = p.getData().getClicksPerSecond();
        cps[3] = cps[3] + i;
        p.getData().setClicksPerSecond(cps);
    }

}
