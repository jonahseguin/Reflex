/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.capturers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.check.data.checkdata.DataRegen;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

@Getter
@Setter
public class CaptureRegen extends RDataCapture implements RTimer{

    @ConfigData("cancel-threshold")
    private int cancelThreshold = 2;

    public CaptureRegen() {
        super(CheckType.REGEN, RCheckType.DATA);
    }

    @EventHandler
    public void onHealth(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player pl = (Player) e.getEntity();
            if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                if(!isCapturing(pl)) return;
                ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
                getData(p).setHealthRegenerated(getData(p).getHealthRegenerated() + e.getAmount());
                if(getData(p).getHealthRegenerated() >= cancelThreshold) {
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
        for(Player pl : Bukkit.getOnlinePlayers()) {
            ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
            if(p.getCapturePlayer().isCapturing(getCheckType())) {
                double health = getData(p).getHealthRegenerated();
                if(health > getData(p).getHps()) {
                    getData(p).setHps(health);
                }
                getData(p).setHealthRegenerated(0);
            }
        }
    }
}
