/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.capturers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.PlayerData;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.event.internal.ReflexUseEntityEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.TrigUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Not an actual RDataCapture;
 * Used to capture generic data for players than can be used for Triggers, etc.
 *
 * Should never check if #isCapturing in this class
 */
public class CapturePlayerData extends RDataCapture {

    @ConfigData("max-target-distance")
    private double maxTargetDistance = 4.5D;

    public CapturePlayerData(Reflex instance) {
        super(instance, CheckType.PLAYER_DATA, RCheckType.DATA);
    }

    @EventHandler
    public void onUseEntity(ReflexUseEntityEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = getPlayer(p);
        PlayerData data = rp.getData();

        if (data.target != null && data.target.getWorld().equals(p.getWorld()) &&
                data.target.getLocation().distance(p.getLocation()) < maxTargetDistance) {
            double aimValue = Math.abs(TrigUtils.getDirection(p.getLocation(), data.target.getLocation()));
            double yawValue = Math.abs(TrigUtils.wrapAngleTo180(p.getLocation().getYaw()));
            double aimOffset = Math.abs(aimValue - yawValue);

            rp.getData().setAimValue(aimValue);
            rp.getData().setAimYawValue(yawValue);
            rp.getData().setAimOffset(aimOffset);
        }
    }

}
