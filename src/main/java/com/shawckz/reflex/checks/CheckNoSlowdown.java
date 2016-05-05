package com.shawckz.reflex.checks;

import com.google.common.collect.ImmutableList;
import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.Check;
import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class CheckNoSlowdown extends Check {
    private final static double SPEED = 4.317;
    private final List<Material> slowdownBlocks;
    public CheckNoSlowdown() {
        super(CheckType.NO_SLOW_DOWN);
        slowdownBlocks = ImmutableList.of(Material.SOUL_SAND, Material.WEB, Material.WATER);
    }
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Block block = event.getFrom().getBlock();
        if (slowdownBlocks.contains(block.getRelative(BlockFace.DOWN).getType()) || slowdownBlocks.contains(block.getType())) {
            final ReflexPlayer player = ReflexCache.get().getAresPlayer(event.getPlayer());
            if (player.getData().getBlocksPerSecond() < SPEED) {
                return;
            }
            Bukkit.getScheduler().runTaskLater(Reflex.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (player.getData().getBlocksPerSecond() < SPEED) {
                        fail(player);
                    }
                }
            },20);
        }
    }
}
