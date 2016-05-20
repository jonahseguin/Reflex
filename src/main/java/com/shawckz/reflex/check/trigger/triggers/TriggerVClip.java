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
import lombok.Getter;
import lombok.Setter;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

@Getter
@Setter
public class TriggerVClip extends RTrigger {

    @ConfigData("capture-time")
    private int captureTime = 10;

    public TriggerVClip() {
        super(CheckType.VCLIP, RCheckType.TRIGGER);
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
        Distance distance = new Distance(e.getFrom(), e.getTo());
        double to = Math.round(distance.toY());
        if (Math.round(distance.getYDifference()) < 2) {
            return;
        }
        for (int i = 0; i < Math.round(distance.getYDifference()) + 1; i++) {
            Block block = new Location(pl.getWorld(), pl.getLocation().getX(), to + i, pl.getLocation().getZ()).getBlock();
            if ((block.getType() != Material.AIR) && (block.getType().isSolid())) {
                p.getData().setTriedVClip(true);
                p.getData().setVclipY(e.getTo().getBlockY());
                p.getData().setLastVClipLocation(e.getFrom());
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMoveSameY(PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player pl = e.getPlayer();
        ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
        if ((e.getTo().getBlockX() == e.getFrom().getBlockX()) && (e.getTo().getBlockY() == e.getFrom().getBlockY()) && (e.getTo().getBlockZ() == e.getFrom().getBlockZ())) {
            return;
        }
        if (p.getData().isTriedVClip()) {
            if (p.getData().getVclipY() == e.getTo().getBlockY()) {
                p.getData().setTriedVClip(false);
                p.getData().setVclipY(-1);
                if (triggerLater(p, result -> {})) {
                    //Can cancel
                    e.setTo(p.getData().getLastVClipLocation());
                }
                p.getData().setLastVClipLocation(null);
            }
            else {
                p.getData().setTriedVClip(false);
                p.getData().setVclipY(-1);
                p.getData().setLastVClipLocation(null);
            }
        }
    }

}
