/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.capturers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.check.data.checkdata.DataVClip;
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
public class CaptureVClip extends RDataCapture {


    public CaptureVClip(Reflex instance) {
        super(instance, CheckType.VCLIP, RCheckType.DATA);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!isCapturing(e.getPlayer())) return;
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
                    getData(p).setTriedVClip(true);
                    getData(p).setVclipY(e.getTo().getBlockY());
                    getData(p).setLastVClipLocation(e.getFrom());
                    break;
                }
            }
        }
        else {
            //VClip up
            for (int i = 0; i < Math.round(distance.getYDifference()) + 1; i++) {
                Block block = new Location(pl.getWorld(), pl.getLocation().getX(), to - i, pl.getLocation().getZ()).getBlock();
                if ((block.getType() != Material.AIR) && (block.getType().isSolid())) {
                    getData(p).setTriedVClip(true);
                    getData(p).setVclipY(e.getTo().getBlockY());
                    getData(p).setLastVClipLocation(e.getFrom());
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMoveSameY(PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!isCapturing(e.getPlayer())) return;
        Player pl = e.getPlayer();
        ReflexPlayer p = getPlayer(pl);
        if ((e.getTo().getBlockX() == e.getFrom().getBlockX()) && (e.getTo().getBlockY() == e.getFrom().getBlockY()) && (e.getTo().getBlockZ() == e.getFrom().getBlockZ())) {
            return;
        }
        if (getData(p).isTriedVClip()) {
            if (getData(p).getVclipY() == e.getTo().getBlockY()) {
                getData(p).setTriedVClip(false);
                getData(p).setVclipY(-1);
                getData(p).setVclipAttempts(getData(p).getVclipAttempts() + 1);
                getData(p).setLastVClipLocation(null);
            }
            else {
                getData(p).setTriedVClip(false);
                getData(p).setVclipY(-1);
                getData(p).setLastVClipLocation(null);
            }
        }
    }

    private DataVClip getData(ReflexPlayer reflexPlayer) {
        return (DataVClip) reflexPlayer.getCapturePlayer().getData(getCheckType());
    }

}
