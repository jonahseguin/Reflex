/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data.capturers;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.data.RDataCapture;
import com.jonahseguin.reflex.oldchecks.data.checkdata.DataAutoClick;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
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
public class CaptureAutoClick extends RDataCapture {

    public CaptureAutoClick(Reflex instance) {
        super(instance, CheckType.AUTO_CLICK, RCheckType.DATA);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent e) {
        if (!isCapturing(e.getPlayer())) return;
        Player pl = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            ReflexPlayer p = getPlayer(pl);
            updateCps(p, 1);//Add 1 to their most recent CPS
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        if (!isCapturing(e.getPlayer())) return;
        Player pl = e.getPlayer();
        ReflexPlayer p = getPlayer(pl);
        updateCps(p, -0.5D);
    }

    private void updateCps(ReflexPlayer p, double i) {
        DataAutoClick dataAutoClick = getData(p);
        dataAutoClick.setClicks(dataAutoClick.getClicks() + i);
    }

    private DataAutoClick getData(ReflexPlayer reflexPlayer) {
        return (DataAutoClick) reflexPlayer.getCapturePlayer().getData(getCheckType());
    }

}
