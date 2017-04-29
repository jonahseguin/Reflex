/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.triggers;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.base.RTimer;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
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

    public TriggerRegen(Reflex instance) {
        super(instance, CheckType.REGEN, RCheckType.TRIGGER);
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
                triggerLater(player, result -> {

                });
            }

            player.getData().setHealthPerSecond(0);
        });
    }
}
