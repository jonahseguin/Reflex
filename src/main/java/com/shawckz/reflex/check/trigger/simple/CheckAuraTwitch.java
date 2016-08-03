/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.event.internal.ReflexUseEntityEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.TrigUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CheckAuraTwitch extends RTrigger {

    @ConfigData("minimum-consecutive-twitches")
    private int minTwitches = 2;

    @ConfigData("max-target-distance")
    private double maxTargetDistance = 4.5D;

    @ConfigData("min-target-distance")
    private double minTargetDistance = 1.5;

    @ConfigData("max-yaw-offset")
    private double maxYawOffset = 30.0D;

    public CheckAuraTwitch(Reflex instance) {
        super(instance, CheckType.AURA_TWITCH, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onUseEntity(ReflexUseEntityEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = getPlayer(p);

        if (rp.getData().getTarget() != null && rp.getData().getTarget().getWorld().equals(p.getWorld())) {
            double distance = rp.getData().getTarget().getLocation().distance(p.getLocation());
            if (distance < maxTargetDistance && distance >= minTargetDistance) {
                double aimValue = Math.abs(TrigUtils.getDirection(p.getLocation(), rp.getData().getTarget().getLocation()));
                double yawValue = Math.abs(TrigUtils.wrapAngleTo180(p.getLocation().getYaw()));
                double difference = Math.abs(aimValue - yawValue);

                if (difference <= maxYawOffset) {//Looking at target


                    rp.getData().setLastYaw(p.getLocation().getYaw());
                }
                else {
                    //Not looking at target
                }
            }

        }

    }

    private float difference(float a, float b) {
        return (a > b ? a - b : b - a);
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }
}
