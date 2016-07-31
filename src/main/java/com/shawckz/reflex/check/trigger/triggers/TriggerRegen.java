/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

@Getter
@Setter
public class TriggerRegen extends RTrigger implements RTimer {

    @ConfigData(value = "max-health-per-second")
    private int maxHps = 2;

    @ConfigData(value = "health-per-second-threshold")
    private int threshold = 8;

    @ConfigData("capture-time")
    private int captureTime = 30;

    public TriggerRegen() {
        super(CheckType.REGEN, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onHealth(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player pl = (Player) e.getEntity();
            if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
                p.getData().setHealthPerSecond(p.getData().getHealthPerSecond() + e.getAmount());
                /* -- Remove because too spammy
                if(p.getData().getHealthPerSecond() >= threshold) {
                    if(fail(p).isCancelled()) {
                        e.setCancelled(true);
                    }
                }*/
            }
        }
    }

    @Override
    public void runTimer() {
        Reflex.getOnlinePlayers().forEach(player -> {
            if (player.getData().getHealthPerSecond() > maxHps) {
                triggerLater(player, result -> {

                });
            }

            player.getData().setHealthPerSecond(0);
        });
    }
}
