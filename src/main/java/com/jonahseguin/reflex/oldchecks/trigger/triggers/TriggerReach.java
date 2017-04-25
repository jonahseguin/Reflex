/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.triggers;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Getter
@Setter
public class TriggerReach extends RTrigger {

    @ConfigData(value = "max-distance")
    private double maxDistance = 6;

    @ConfigData("capture-time")
    private int captureTime = 10;

    public TriggerReach(Reflex instance) {
        super(instance, CheckType.REACH, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player d = (Player) e.getDamager();

            if (p.getLocation().getBlockY() == d.getLocation().getBlockY()) {
                double distance = p.getLocation().distance(d.getLocation());

                if (distance > maxDistance) {
                    triggerLater(getPlayer(d), result -> {

                    });
                }

            }

        }
    }


}
