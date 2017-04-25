/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.simple;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.event.internal.ReflexUseEntityEvent;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Getter
@Setter
public class CheckAccuracy extends RTrigger {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @ConfigData("minimum-hits")
    private int minHits = 20;

    @ConfigData("minimum-consecutive-hits")
    private int minConsecutiveHits = 10;

    @ConfigData("minimum-accuracy") // range [0.0 -> 1.0]
    private double minAccuracy = 0.70D;

    @ConfigData("fail-threshold")
    private int failThreshold = 2;

    @ConfigData("max-yaw-offset")
    private double maxYawOffset = 30.0D;

    //last hit time, last miss time, total hits, total misses, consecutive hits (hits in a row), consecutive misses (misses in a row), lastTarget

    public CheckAccuracy(Reflex instance) {
        super(instance, CheckType.ACCURACY, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onUseEntity(ReflexUseEntityEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = getPlayer(p);

        if (rp.getData().getTarget() != null && rp.getData().getTarget().getWorld().equals(p.getWorld()) &&
                rp.getData().getTarget().getLocation().distance(p.getLocation()) < 4.5D) { //Make configurable?

            double difference = rp.getData().getAimOffset();

            if (difference <= maxYawOffset) {//Looking at target
                rp.getData().setHits(rp.getData().getHits() + 1);
                rp.getData().setConsecutiveHits(rp.getData().getConsecutiveHits() + 1);
                rp.getData().setConsecutiveMisses(0);

                if (rp.getData().getHits() >= minHits && rp.getData().getConsecutiveHits() >= minConsecutiveHits) {
                    double accuracy = calculateAccuracy(rp.getData().getHits(), rp.getData().getMisses());
                    if (accuracy >= minAccuracy) {
                        rp.addAlertVL(getCheckType());
                        if (rp.getAlertVL(getCheckType()) >= failThreshold) {
                            fail(rp, df.format(accuracy * 100) + "% accuracy");
                            rp.setAlertVL(getCheckType(), 0);
                        }
                    }
                    else {
                        rp.modifyAlertVL(getCheckType(), -1);
                    }
                }

            }
            else {
                //Not looking at target
                miss(rp);
            }

        }
        else {
            //Missed
            miss(rp);
        }

    }

    public double calculateAccuracy(int hits, int misses) {
        if (hits == 0 && misses == 0) {
            return 0.0D;
        }
        else if (hits == 0) {
            return 0.0D;
        }
        else if (misses == 0) {
            return 1.0D;
        }
        return hits / misses;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player d = (Player) e.getDamager();
            ReflexPlayer rp = getPlayer(d);
            if (!rp.getData().getLastTarget().equals(p)) {
                rp.getData().setConsecutiveHits(0);
                rp.getData().setConsecutiveMisses(0);
                rp.getData().setHits(0);
                rp.getData().setMisses(0);
                //Reset when they start attacking a new player
            }
        }
    }

    private void miss(ReflexPlayer rp) {
        rp.getData().misses += 1;
        rp.getData().setConsecutiveHits(0);
        rp.getData().consecutiveMisses += 1;
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }
}
