/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.triggers;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

@Getter
@Setter
public class TriggerFly extends RTrigger {

    @ConfigData("capture-time")
    private int captureTime = 10;

    //AirTime
    @ConfigData("trigger-air-time")
    private int triggerAirTime = 3;

    @ConfigData("threshold-air-time")
    private int thresholdAirTime = 15;

    public TriggerFly(Reflex instance) {
        super(instance, CheckType.FLY, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!isEnabled()) return;
        if (e.getPlayer().getAllowFlight()) return;
        if (e.getPlayer().getVehicle() != null) return;

        ReflexPlayer rp = getPlayer(e.getPlayer());

        if (e.getTo().getY() >= e.getFrom().getY()) {
            rp.getData().setHasPositiveVelocity(true);
        } else {
            rp.getData().setHasPositiveVelocity(false);
        }

        if (rp.getData().isOnGround()) {
            rp.getData().setLastGroundTime(System.currentTimeMillis());
        } else {
            if (rp.getData().getLastGroundTimeUpdate() != -1 && ((System.currentTimeMillis() - rp.getData().getLastGroundTimeUpdate()) / 1000 < triggerAirTime)) {
                if (rp.getData().getLastGroundTime() != -1) {
                    int airTime = (int) (System.currentTimeMillis() - rp.getData().getLastGroundTime()) / 1000;
                    if (rp.getData().isHasPositiveVelocity()) {
                        if (airTime >= triggerAirTime) {
                            triggerLater(rp, result -> {
                            });
                        }
                    } else {
                        if (airTime >= thresholdAirTime) {
                            fail(rp, "> " + thresholdAirTime + "s in air");
                        }
                    }
                }
            }
        }

        rp.getData().setLastGroundTimeUpdate(System.currentTimeMillis());
    }


}
