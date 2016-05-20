/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.capturers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.check.data.checkdata.DataReach;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.AbstractMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Getter
@Setter
public class CaptureReach extends RDataCapture {

    @ConfigData("cancel-threshold")
    private int cancelThreshold = 2;

    public CaptureReach() {
        super(CheckType.REACH, RCheckType.DATA);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player d = (Player) e.getDamager();

            if(!isCapturing(d)) return;

            if(p.getLocation().getBlockY() == d.getLocation().getBlockY()) {
                double distance = p.getLocation().distance(d.getLocation());
                int ping = ((CraftPlayer)d).getHandle().ping;
                Bukkit.getLogger().info("(CAPTURE) " + d.getName() + " hit " + d.getName() + " from " + distance);

                ReflexPlayer rd = Reflex.getInstance().getCache().getReflexPlayer(d);

                getData(rd).getEntries().add(new AbstractMap.SimpleEntry<>(distance, ping));

                if(getData(rd).getPeakReach() < distance) {
                    getData(rd).setPeakReach(distance);
                }
            }
        }
    }

    private DataReach getData(ReflexPlayer reflexPlayer) {
        return (DataReach) reflexPlayer.getCapturePlayer().getData(getCheckType());
    }

}
