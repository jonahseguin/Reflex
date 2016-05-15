/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.capturers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.check.data.checkdata.DataAutoClick;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Lag;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
@Setter
public class CaptureAutoClick extends RDataCapture implements RTimer {


    public CaptureAutoClick() {
        super(CheckType.AUTO_CLICK, RCheckType.DATA);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent e) {
        if(!isCapturing(e.getPlayer())) return;
        Player pl = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
            updateCps(p, 1);//Add 1 to their most recent CPS
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        if(!isCapturing(e.getPlayer())) return;
        Player pl = e.getPlayer();
        ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
        updateCps(p, -0.5D);
    }

    private void updateCps(ReflexPlayer p, double i) {
        DataAutoClick dataAutoClick = getData(p);
        dataAutoClick.setClicks(dataAutoClick.getClicks() + i);
    }

    @Override
    public void runTimer() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (isCapturing(pl)) {
                ReflexPlayer player = Reflex.getInstance().getCache().getReflexPlayer(pl);

                //Update ping & tps
                getData(player).setPing(((CraftPlayer) pl).getHandle().ping);
                getData(player).setTps(Lag.getTPS());
            }
        }
    }

    private DataAutoClick getData(ReflexPlayer reflexPlayer) {
        return (DataAutoClick) reflexPlayer.getCapturePlayer().getData(getCheckType());
    }

}
