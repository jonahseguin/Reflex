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
 * Created by Jonah Seguin on Sun 2017-04-30 at 17:38.
 * Project: Reflex
 */
public class CheckBlockHit extends Check {

    @ConfigData("min-attempts")
    private int minAttempts = 5;

    public CheckBlockHit(Reflex reflex) {
        super(reflex, CheckType.BLOCK_HIT);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockHitMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);
        if (rp.getData().isOnGround() && player.isSprinting() && player.isBlocking()) {
            rp.addAlertVL(getCheckType());
            if (rp.getAlertVL(getCheckType()) >= minAttempts) {
                fail(rp).cancelIfAllowed(event);
            }
        } else if (player.isSprinting() && !player.isBlocking()) {
            rp.setAlertVL(getCheckType(), 0);
        }
    }

}
