/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.oldchecks;

public class CheckNoSlowdown {

    /*

    private final static double SPEED = 4.317;
    private final List<Material> slowdownBlocks;

    public CheckNoSlowdown() {
        super(CheckType.NO_SLOW_DOWN);
        slowdownBlocks = ImmutableList.of(Material.SOUL_SAND, Material.WEB, Material.WATER);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Block block = event.getFrom().getBlock();
        if (slowdownBlocks.contains(block.getRelative(BlockFace.DOWN).getType()) || slowdownBlocks.contains(block.getType())) {
            final ReflexPlayer player = ReflexCache.get().getReflexPlayer(event.getPlayer());
            if (player.getData().getBlocksPerSecond() < SPEED) {
                return;
            }
            Bukkit.getScheduler().runTaskLater(Reflex.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (player.getData().getBlocksPerSecond() < SPEED) {
                        fail(player);
                    }
                }
            }, 20);
        }
    }

    */

}
