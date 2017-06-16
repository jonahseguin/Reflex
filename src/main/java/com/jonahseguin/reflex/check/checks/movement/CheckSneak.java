/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.movement;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jonah Seguin on Tue 2017-05-09 at 20:23.
 * Project: Reflex
 */
public class CheckSneak extends Check {

    @ConfigData("minimum-attempts")
    private int minAttempts = 3;

    public CheckSneak(Reflex reflex) {
        super(reflex, CheckType.SNEAK);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMoveSneak(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);
        if (player.isSprinting() && player.isSneaking()) {
            rp.addPreVL(getCheckType());
            if (rp.getPreVL(getCheckType()) >= minAttempts) {
                fail(rp, "n/a").cancelIfAllowed(event);
                rp.setPreVL(getCheckType(), 0);
            }
        }
    }

}
