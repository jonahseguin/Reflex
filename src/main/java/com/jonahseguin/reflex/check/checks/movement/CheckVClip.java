/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.movement;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Distance;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:54.
 * Project: Reflex
 */
public class CheckVClip extends Check {

    @ConfigData("capture-time")
    private int captureTime = 10;

    public CheckVClip(Reflex instance) {
        super(instance, CheckType.VCLIP);
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
        ReflexPlayer p = getPlayer(pl);
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
        if (e.getTo().getBlockY() < e.getFrom().getBlockY()) {
            //VClip down
            for (int i = 0; i < Math.round(distance.getYDifference()) + 1; i++) {
                Block block = new Location(pl.getWorld(), pl.getLocation().getX(), to + i, pl.getLocation().getZ()).getBlock();
                if ((block.getType() != Material.AIR) && (block.getType().isSolid())) {
                    p.getData().setTriedVClip(true);
                    p.getData().setVclipY(e.getTo().getBlockY());
                    p.getData().setLastVClipLocation(e.getFrom());
                    break;
                }
            }
        } else {
            //VClip up
            for (int i = 0; i < Math.round(distance.getYDifference()) + 1; i++) {
                Block block = new Location(pl.getWorld(), pl.getLocation().getX(), to - i, pl.getLocation().getZ()).getBlock();
                if ((block.getType() != Material.AIR) && (block.getType().isSolid())) {
                    p.getData().setTriedVClip(true);
                    p.getData().setVclipY(e.getTo().getBlockY());
                    p.getData().setLastVClipLocation(e.getFrom());
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMoveSameY(PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player pl = e.getPlayer();
        ReflexPlayer p = getPlayer(pl);
        if ((e.getTo().getBlockX() == e.getFrom().getBlockX()) && (e.getTo().getBlockY() == e.getFrom().getBlockY()) && (e.getTo().getBlockZ() == e.getFrom().getBlockZ())) {
            return;
        }
        if (p.getData().isTriedVClip()) {
            if (p.getData().getVclipY() == e.getTo().getBlockY()) {
                p.getData().setTriedVClip(false);
                p.getData().setVclipY(-1);
                if (fail(p).canCancel()) {
                    //Can cancel
                    e.setTo(p.getData().getLastVClipLocation());
                }
                p.getData().setLastVClipLocation(null);
            } else {
                p.getData().setTriedVClip(false);
                p.getData().setVclipY(-1);
                p.getData().setLastVClipLocation(null);
            }
        }
    }

}
