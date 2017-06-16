/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.movement;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:45.
 * Project: Reflex
 */
public class CheckNoFall extends Check {

    @ConfigData("minimum-fall-distance")
    private double minFallDistance = 3.0;

    @ConfigData("minimum-attempts")
    private int minAttempts = 3;

    public CheckNoFall(Reflex instance) {
        super(instance, CheckType.NO_FALL);
    }

    @EventHandler
    public void onNoFallMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);
        if (canCheck(rp)) {
            if (!rp.getData().isOnGround() && event.getTo().getY() < event.getFrom().getY()) {
                // Falling downwards
                rp.getData().fallDistance += (event.getFrom().getY() - event.getTo().getY());
            }
            if (rp.getData().fallDistance >= minFallDistance) {
                if (player.getFallDistance() == 0.0f) {
                    rp.addPreVL(getCheckType());
                    if (rp.getPreVL(getCheckType()) >= minAttempts) {
                        // Fail & reset
                        fail(rp, Math.round(rp.getData().fallDistance) + " fall");
                        rp.setPreVL(getCheckType(), 0);
                        rp.getData().fallDistance = 0;
                    }
                } else {
                    rp.modifyPreVL(getCheckType(), -1);
                }
            }
            if (rp.getData().isOnGround()) {
                rp.getData().fallDistance = 0;
            }
        }
    }

    private boolean canCheck(ReflexPlayer p) {
        Player player = p.getBukkitPlayer();
        if (player.getAllowFlight()) return false;
        if (player.getGameMode() == GameMode.CREATIVE) return false;
        if (player.isInsideVehicle()) return false;
        if (player.getHealth() <= 0) return false;
        return !p.getData().isInLiquid();
    }

    @Override
    public String description() {
        return "Detects when a player does not take fall damage";
    }
}
