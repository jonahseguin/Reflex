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
        Distance distance = new Distance(e.getFrom(), e.getTo());

        if ((int) Math.round(distance.getYDifference()) > 0) {
            return;
        }

        // final double xDistance = e.getTo().getX() - e.getFrom().getX();
        // final double zDistance = e.getTo().getZ() - e.getFrom().getZ();

        // final double hDistance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);

        //Check if they were in block with gravity (sand/gravel) or were in transparent block

        List<Block> from = getBlocks(e.getFrom());
        List<Block> to = getBlocks(e.getTo());

        boolean wasFree = false;
        for (Block b : from) {
            if (b.getType().hasGravity() || !b.getType().isSolid()) {
                wasFree = true;
                break;
            }
        }

        boolean isFree = false;
        for (Block b : to) {
            if (b.getType().hasGravity() || !b.getType().isSolid()) {
                isFree = true;
                break;
            }
        }

        if (wasFree && !isFree) {
            p.getData().setLinkedPhaseAttempts(p.getData().getLinkedPhaseAttempts() + 1);
            p.getData().setTriedPhase(true);
            p.getData().setPhaseY(e.getTo().getBlockY());
            p.getData().setLastPhaseLocation(e.getFrom());
            p.getData().setLastPhaseTime(System.currentTimeMillis());
        }
        else{
            p.getData().setLinkedPhaseAttempts(0);
            p.getData().setTriedPhase(false);
        }


    }

    private List<Block> getBlocks(Location loc) { //Block at player's leg
        List<Block> set = new ArrayList<>();

        set.add(loc.getBlock());
        set.add(loc.clone().add(0, 1, 0).getBlock());

        return set;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
        if(rp.getData().isTriedPhase()) {
            rp.getData().setTriedPhase(false);
            rp.getData().setLinkedPhaseAttempts(0);
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
        if (p.getData().isTriedPhase()) {
            if (p.getData().getPhaseY() == e.getTo().getBlockY()) {
                p.getData().setTriedPhase(false);
                p.getData().setPhaseY(-1);
                //TODO: Implement usage of linked phase attempts && time between phase attempts (add a threshold)
                if (triggerLater(p, result -> {
                })) {
                    //Can cancel
                    e.setTo(p.getData().getLastPhaseLocation());
                }
                p.getData().setLastPhaseLocation(null);
            }
            else {
                p.getData().setTriedPhase(false);
                p.getData().setLinkedPhaseAttempts(0);
                p.getData().setLastPhaseLocation(null);
            }
        }
    }

}
