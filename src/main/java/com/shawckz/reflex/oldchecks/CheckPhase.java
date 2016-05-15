/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.oldchecks;

public class CheckPhase  {

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

        final double xDistance = e.getTo().getX() - e.getFrom().getX();
        final double zDistance = e.getTo().getZ() - e.getFrom().getZ();

        final double hDistance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);

        //  Distance distance = new Distance(e.getFrom(),e.getTo());
        //  double to = Math.round(distance.toY());

        for (int i = 0; i < (Math.round(hDistance)) + 1; i++) {
            Block block = new Location(pl.getWorld(), pl.getLocation().getX(), hDistance + i, pl.getLocation().getZ()).getBlock();
            if (block.getType() != Material.AIR && block.getType().isSolid()) {
                //They moved past a solid block, likely Phase.
                if (fail(p).isCancelled()) {
                    e.setTo(e.getFrom());
                }
            }
        }


    }

*/


}
