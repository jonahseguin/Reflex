/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.capturers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.check.data.checkdata.DataSpeed;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@Setter
public class CaptureSpeed extends RDataCapture implements RTimer {

    public CaptureSpeed() {
        super(CheckType.SPEED, RCheckType.DATA);
    }

    @Override
    public void runTimer() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (isCapturing(pl)) {
                ReflexPlayer player = Reflex.getInstance().getCache().getReflexPlayer(pl);

                DataSpeed dataSpeed = getData(player);

                double totalDistance = 0;

                for (DataSpeed.SpeedData sd : dataSpeed.getBpsData()) {
                    PotionEffect effect = null;
                    for (PotionEffect e : sd.getPotionEffects()) {
                        if (e.getType() == PotionEffectType.SPEED) {
                            effect = e;
                            break;
                        }
                    }

                    double distance = sd.getDistance();

                    if (effect != null) {
                        distance -= (distance) * (effect.getAmplifier() * 0.2);
                    }

                    totalDistance += distance;
                }


                dataSpeed.getBpsData().clear();
                if (totalDistance > dataSpeed.getPeakBlocksPerSecond()) {
                    dataSpeed.setPeakBlocksPerSecond(totalDistance);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (p.getAllowFlight() == true || p.isFlying() || p.getGameMode() == GameMode.CREATIVE) return;
        if (!isCapturing(p)) return;
        ReflexPlayer ap = Reflex.getInstance().getCache().getReflexPlayer(p);

        final double xDistance = e.getTo().getX() - e.getFrom().getX();
        final double zDistance = e.getTo().getZ() - e.getFrom().getZ();

        final double distance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);

        //HORIZONTAL DISTANCE

        if (distance > 0) {
            DataSpeed dataSpeed = getData(ap);
            DataSpeed.SpeedData d = new DataSpeed.SpeedData(p, distance);
            dataSpeed.getData().add(d);
            dataSpeed.getBpsData().add(d);
        }

    }

    private double difference(double a, double b) {
        return (a > b ? a - b : b - a);
    }

    private DataSpeed getData(ReflexPlayer reflexPlayer) {
        return (DataSpeed) reflexPlayer.getCapturePlayer().getData(getCheckType());
    }

}
