/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
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

    public TriggerFly() {
        super(CheckType.FLY, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!isEnabled()) return;
        if (e.getPlayer().getAllowFlight()) return;
        if (e.getPlayer().getVehicle() != null) return;

        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(e.getPlayer());
        if (e.getTo().getY() >= e.getFrom().getY()) {
            rp.getData().setHasPositiveVelocity(true);
        }
        else {
            rp.getData().setHasPositiveVelocity(false);
        }

        if (rp.getData().isOnGround(e.getTo())) {
            rp.getData().setLastGroundtime(System.currentTimeMillis());
        }
        else {
            if (rp.getData().getLastGroundtime() != -1) {
                int airTime = (int) (System.currentTimeMillis() - rp.getData().getLastGroundtime()) / 1000;
                if (rp.getData().isHasPositiveVelocity()) {
                    if (airTime >= triggerAirTime) {
                        triggerLater(rp, result -> {
                        });
                    }
                }
                else {
                    if (airTime >= thresholdAirTime) {
                        fail(rp, "> " + thresholdAirTime + "s in air");
                    }
                }
            }
        }
    }


}
