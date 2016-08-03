/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.event.internal.ReflexUseEntityEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.TrigUtils;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class TriggerAura extends RTrigger {


    @ConfigData("capture-time")
    private int captureTime = 60;

    @ConfigData("maximum-target-distance")
    private double maxTargetDistance = 4.5D;

    @ConfigData("maximum-yaw-offset")
    private double maxYawOffset = 30.0D;

    @ConfigData("pass-vl-penalty")
    private int passPenalty = 2;

    public TriggerAura(Reflex instance) {
        super(instance, CheckType.AURA, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onNotLookingAtTarget(final ReflexUseEntityEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player p = e.getPlayer();
                ReflexPlayer rp = getPlayer(p);

                if (rp.getData().getTarget() != null && rp.getData().getTarget().getWorld().equals(p.getWorld()) &&
                        rp.getData().getTarget().getLocation().distance(p.getLocation()) < maxTargetDistance) {
                    double aimValue = Math.abs(TrigUtils.getDirection(p.getLocation(), rp.getData().getTarget().getLocation()));
                    double yawValue = Math.abs(TrigUtils.wrapAngleTo180(p.getLocation().getYaw()));
                    double difference = Math.abs(aimValue - yawValue);

                    if (difference > maxYawOffset) {//Is looking > X yaw away from target
                        triggerLater(rp, result -> {

                        });
                    }
                    else {
                        rp.modifyAlertVL(getCheckType(), (passPenalty * -1));
                    }

                }
            }
        }.runTaskAsynchronously(getReflex());
    }


    @EventHandler
    public void onUpdateTarget(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            Player target = (Player) e.getEntity();

            ReflexPlayer rp = getPlayer(p);

            rp.getData().setLastTarget(rp.getData().getLastTarget());
            rp.getData().setTarget(target);
        }
    }

}
