/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.other;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.check.RTimer;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:58.
 * Project: Reflex
 */
public class CheckRegen extends Check implements RTimer {

    @ConfigData(value = "max-health-per-second")
    private int maxHps = 2;

    @ConfigData(value = "health-per-second-threshold")
    private int threshold = 8;

    public CheckRegen(Reflex instance) {
        super(instance, CheckType.REGEN);
    }

    @EventHandler
    public void onHealth(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player pl = (Player) e.getEntity();
            if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                ReflexPlayer p = getPlayer(pl);
                p.getData().setHealthPerSecond(p.getData().getHealthPerSecond() + e.getAmount());
                if (p.getData().getHealthPerSecond() >= threshold) {
                    if (this.isCancel()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @Override
    public void runTimer() {
        Reflex.getReflexPlayers().forEach(player -> {
            if (player.getData().getHealthPerSecond() > maxHps) {
                fail(player, Math.round(player.getData().getHealthPerSecond()) + " health/s");
            }

            player.getData().setHealthPerSecond(0);
        });
    }

}
