/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.oldchecks;

public class CheckVClip {

    /*

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        if (e.getTo().getBlockX() == e.getFrom().getBlockX()
                && e.getTo().getBlockY() == e.getFrom().getBlockY()
                && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) {
            return;
        }
        Player pl = e.getPlayer();
        ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);

        if (pl.getGameMode() == GameMode.CREATIVE) return;// Players with creative mode can usually glitch into blocks
        if (pl.getAllowFlight()) return;// Players with fly can also glitch into blocks.  Not necessary
        if (pl.isInsideVehicle() || pl.getVehicle() != null) return;

        Distance distance = new Distance(e.getFrom(), e.getTo());
        double to = Math.round(distance.toY());
        if (Math.round(distance.getYDifference()) < 2) {
            return;// VClip requires at least 2 different, as vclip only works
        }
        // With at least a 2 block change

        for (int i = 0; i < (Math.round(distance.getYDifference())) + 1; i++) {
            Block block = new Location(pl.getWorld(), pl.getLocation().getX(), to + i, pl.getLocation().getZ()).getBlock();
            if (block.getType() != Material.AIR && block.getType().isSolid()) {
                //They moved past a solid block, likely VCLIP.
                if (fail(p).isCancelled()) {
                    e.setTo(e.getFrom());
                }
            }
        }
    }

    */

}
