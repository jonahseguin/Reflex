/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data.capturers;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.base.RTimer;
import com.jonahseguin.reflex.oldchecks.data.RDataCapture;
import com.jonahseguin.reflex.oldchecks.data.checkdata.DataRegen;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

@Getter
@Setter
public class CaptureRegen extends RDataCapture implements RTimer {

    @ConfigData("cancel-threshold")
    private int cancelThreshold = 2;

    public CaptureRegen(Reflex instance) {
        super(instance, CheckType.REGEN, RCheckType.DATA);
    }

    @EventHandler
    public void onHealth(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player pl = (Player) e.getEntity();
            if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                if (!isCapturing(pl)) return;
                ReflexPlayer p = getPlayer(pl);
                getData(p).setHealthRegenerated(getData(p).getHealthRegenerated() + e.getAmount());
                if (getData(p).getHealthRegenerated() >= cancelThreshold) {
                    e.setCancelled(true);
                }
            }
        }
    }

    private DataRegen getData(ReflexPlayer reflexPlayer) {
        return (DataRegen) reflexPlayer.getCapturePlayer().getData(getCheckType());
    }

    @Override
    public void runTimer() {
        Reflex.getReflexPlayers().forEach(p -> {
            if (p.getCapturePlayer().isCapturing(getCheckType())) {
                double health = getData(p).getHealthRegenerated();
                if (health > getData(p).getHps()) {
                    getData(p).setHps(health);
                }
                getData(p).setHealthRegenerated(0);
            }
        });
    }
}
