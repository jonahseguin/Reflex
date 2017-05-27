/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.other;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.text.DecimalFormat;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 17:02.
 * Project: Reflex
 */
public class CheckFastEat extends Check {

    private final DecimalFormat df = new DecimalFormat("##.##");
    @ConfigData("min-attempts")
    private int minAttempts = 2;
    @ConfigData("minimum-pass-interact-ms")
    private long maxInteractDifference = 50L; // 1/20th of a second
    @ConfigData("minimum-tps")
    private double minTps = 15;
    @ConfigData("max-ping")
    private int maxPing = 400;

    public CheckFastEat(Reflex reflex) {
        super(reflex, CheckType.FAST_EAT);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFastEatConsume(final PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);
        rp.getData().eatConsume = System.currentTimeMillis();
        if (!rp.getData().eatDidInteract()) {
            if (Lag.getTPS() >= minTps && rp.getPing() < maxPing) {
                long difference = System.currentTimeMillis() - rp.getData().eatInteract;
                if (difference <= maxInteractDifference) {
                    rp.addPreVL(getCheckType());
                    if (rp.getPreVL(getCheckType()) >= minAttempts) {
                        fail(rp, df.format(maxInteractDifference) + "ms diff.").cancelIfAllowed(event);
                    } else {
                        rp.setPreVL(getCheckType(), 0);
                    }
                }
            }
        } else {
            rp.addPreVL(getCheckType());
        }

        if (rp.getPreVL(getCheckType()) >= minAttempts) {
            fail(rp, "No interact ").cancelIfAllowed(event);
            rp.setPreVL(getCheckType(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFastEatInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.hasItem()) {
                if (event.getItem().getType().isEdible()) {
                    rp.getData().eatMaterial = event.getItem().getType();
                    rp.getData().eatInteract = System.currentTimeMillis();
                }
            }
        }
    }

    // on Drop
    @EventHandler(ignoreCancelled = true)
    public void onDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);
        rp.getData().eatInteract = 0;
    }

    // on switch item
    @EventHandler
    public void onFastEatSwapItem(final PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);
        rp.getData().eatInteract = 0;
    }

}
