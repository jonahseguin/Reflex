/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class CheckSmoothAim extends RTrigger implements RTimer {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @ConfigData("max-aimspeed-difference")
    private float maxAimSpeedDifference = 3;

    @ConfigData("minimum-aimspeed")
    private float minAimSpeed = 20F;

    public CheckSmoothAim() {
        super(CheckType.SMOOTH_AIM, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);

        float aimSpeed = difference(e.getTo().getYaw(), e.getFrom().getYaw());

        rp.getData().setAimSpeed(rp.getData().getAimSpeed() + aimSpeed);

    }

    @Override
    public void runTimer() {
        Reflex.getReflexPlayers().forEach(rp -> {
            float aimSpeed = rp.getData().getAimSpeed();
            float lastAimSpeed = rp.getData().getLastAimSpeed();

            if (aimSpeed >= minAimSpeed && lastAimSpeed >= minAimSpeed) {
                if (difference(aimSpeed, lastAimSpeed) <= maxAimSpeedDifference) {
                    fail(rp, df.format(aimSpeed) + " aim speed");
                }
            }

            rp.getData().setLastAimSpeed(aimSpeed);
        });
    }

    private float difference(float a, float b) {
        return (a > b ? a - b : b - a);
    }

    @Override
    public int getCaptureTime() {
        return 0;
    }
}
