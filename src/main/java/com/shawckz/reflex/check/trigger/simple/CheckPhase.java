/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.google.common.collect.Lists;
import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Gate;

/**
 * Created by jonahseguin on 2016-07-28.
 */
@Getter
@Setter
public class CheckPhase extends RTrigger {

    private double box = 0.42500001192093;
    private double radi = box / 2;
    @ConfigData("translucent-materials")
    private List<Material> translucentMaterials = Lists.newArrayList();

    public CheckPhase() {
        super(CheckType.PHASE, RCheckType.TRIGGER);
    }

    public void addToList(Material mat) {
        this.translucentMaterials.add(mat);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();

        if (from.getX() == to.getX() && from.getZ() == to.getZ()) {
            return;
        }

        Block b = from.getBlock();
        Block b1 = from.getBlock().getRelative(BlockFace.UP);


        if (b.getType() != Material.FENCE_GATE || b1.getType() != Material.FENCE_GATE) {
            return;
        }

        Gate gate = (Gate) b.getState().getData();
        Gate gate1 = (Gate) b1.getState().getData();

        if (gate.isOpen() && gate1.isOpen()) {
            return;
        }

        double fromy = -1;
        double toy = -1;
        double middle = -1;

        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);

        switch (gate.getFacing()) {
            case NORTH:
            case SOUTH:
                fromy = from.getX();
                toy = to.getX();
                middle = from.getBlockX() + 0.5;
                break;
            case EAST:
            case WEST:
                fromy = from.getZ();
                toy = to.getZ();
                middle = from.getBlockZ() + 0.5;
                break;
            default:
                fromy = 0;
                toy = 0;
                break;
        }

        if (fromy == -1 || toy == -1 || middle == -1) {
            return;
        }

        fromy = Math.abs(fromy);
        toy = Math.abs(toy);
        middle = Math.abs(middle);

        if ((toy < middle && middle < fromy) || (fromy < middle && middle < toy)) {
            if (fail(rp, "Type 1").isCancelled()) {
                e.setTo(e.getFrom());
            }
            return;
        }

        double plusb = middle + radi;
        double negb = middle - radi;

        if (fromy < negb) {
            if (toy >= negb) {
                if ((toy < middle && middle < fromy) || (fromy < middle && middle < toy)) {
                    if (fail(rp, "Type 2A").isCancelled()) {
                        e.setTo(e.getFrom());
                    }
                    return;
                }
            }
            if (fromy > plusb) {
                if (toy <= plusb) {
                    if ((toy < middle && middle < fromy) || (fromy < middle && middle < toy)) {
                        if (fail(rp, "Type 2B").isCancelled()) {
                            e.setTo(e.getFrom());
                        }
                        return;
                    }
                }
                if (isBetween(fromy, middle, radi) && !isBetween(toy, middle, radi)) {
                    if (fail(rp, "Type 3A").isCancelled()) {
                        e.setTo(e.getFrom());
                    }
                }
                else if (!isBetween(fromy, middle, radi) && isBetween(toy, middle, radi)) {
                    if (fail(rp, "Type 3B").isCancelled()) {
                        e.setTo(e.getFrom());
                    }
                }
            }
        }
    }

    public boolean isBetween(double number, double middle, double rad) {
        double maxbound = middle + rad;
        double minbound = middle - rad;

        return minbound < number && number < maxbound;
    }


    @EventHandler(ignoreCancelled = true)
    public void denyConventionalPhase(PlayerMoveEvent event) {
        // Do nothing if player is flying
        Player player = event.getPlayer();
        if (player.isFlying()) return;

        // Do nothing if player is going above sky limit
        Location t = event.getTo();
        if (t.getY() > 254) return;

        // Do nothing if player has not moved
        Location f = event.getFrom();
        double distance = f.distanceSquared(t);
        if (distance == 0.0D) return;

        // Deny movement if player has moved too far to prevent excessive lookups
        if (distance > 64.0D) {
            event.setTo(f.setDirection(t.getDirection()));
            return;
        }

        // Calculate all possible blocks the player has moved through
        int topBlockX = f.getBlockX() < t.getBlockX() ? t.getBlockX() : f.getBlockX();
        int bottomBlockX = f.getBlockX() > t.getBlockX() ? t.getBlockX() : f.getBlockX();

        int topBlockY = (f.getBlockY() < t.getBlockY() ? t.getBlockY() : f.getBlockY()) + 1;
        int bottomBlockY = f.getBlockY() > t.getBlockY() ? t.getBlockY() : f.getBlockY();
        if (player.isInsideVehicle()) bottomBlockY++;

        int topBlockZ = f.getBlockZ() < t.getBlockZ() ? t.getBlockZ() : f.getBlockZ();
        int bottomBlockZ = f.getBlockZ() > t.getBlockZ() ? t.getBlockZ() : f.getBlockZ();

        // Iterate through the outermost coordinates
        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    // Do nothing if material is able to be moved through
                    if (translucentMaterials.contains(f.getWorld().getBlockAt(x, y, z).getType())) continue;

                    // Do nothing if player has walked over stairs
                    if (y == bottomBlockY && f.getBlockY() != t.getBlockY()) continue;

                    // Deny movement
                    event.setTo(f.setDirection(t.getDirection()));
                }
            }
        }
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }
}
