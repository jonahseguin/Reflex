/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Distance;
import com.shawckz.reflex.util.obj.TrigUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@Getter
@Setter
public class TriggerPhase extends RTrigger {

    @ConfigData("capture-time")
    private int captureTime = 10;

    public TriggerPhase() {
        super(CheckType.PHASE, RCheckType.TRIGGER);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if ((e.getTo().getBlockX() == e.getFrom().getBlockX()) && (e.getTo().getBlockY() == e.getFrom().getBlockY()) && (e.getTo().getBlockZ() == e.getFrom().getBlockZ())) {
            return;
        }
        Player pl = e.getPlayer();
        ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
        if (pl.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (pl.getAllowFlight()) {
            return;
        }
        if ((pl.isInsideVehicle()) || (pl.getVehicle() != null)) {
            return;
        }

    }

}
