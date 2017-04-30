/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.movement;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedLeaveEvent;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:55.
 * Project: Reflex
 */
public class CheckBedFly extends Check {

    @ConfigData("minimum-attempts")
    private int minAttempts = 3;

    public CheckBedFly(Reflex reflex) {
        super(reflex, CheckType.BED_FLY);
    }

    @EventHandler
    public void onBedFlyBedLeave(final PlayerBedLeaveEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);

        for (Block block : rp.getData().getBlocksAround(player.getLocation(), 10)) {
            Material m = block.getType();
            if (m == Material.BED || m == Material.BED_BLOCK) {
                return;
            }
        }

        rp.addAlertVL(getCheckType());

        if (rp.getAlertVL(getCheckType()) >= this.minAttempts) {
            if (fail(rp).canCancel()) {
                player.teleport(player.getLocation());
            }
        }

    }

    @Override
    public String description() {
        return "Detects when a player 'leaves' a bed without a bed nearby";
    }
}
