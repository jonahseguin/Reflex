/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Getter
@Setter
public class TriggerReach extends RTrigger {

    @ConfigData(value = "max-distance")
    private double maxDistance = 4.475;

    @ConfigData("capture-time")
    private int captureTime = 10;

    public TriggerReach() {
        super(CheckType.REACH, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player d = (Player) e.getDamager();

            if(p.getLocation().getBlockY() == d.getLocation().getBlockY()) {
                double distance = p.getLocation().distance(d.getLocation());
                Bukkit.getLogger().info(d.getName() + " hit " + d.getName() + " from " + distance);

                if(distance > maxDistance) {
                    triggerLater(Reflex.getInstance().getCache().getReflexPlayer(d), result -> {

                    });
                }

            }

        }
    }


}
