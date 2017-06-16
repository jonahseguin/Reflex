/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.combat;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:57.
 * Project: Reflex
 */
public class CheckReach extends Check {

    @ConfigData("base-max-reach")
    private double baseMaxReach = 4.0D;

    @ConfigData("max-velocity-length")
    private double maxVelocityLength = 0.08D;

    @ConfigData("min-attempts")
    private double minAttempts = 3; // consecutive

    @ConfigData("max-reach-time-difference-milliseconds")
    private double maxTimeDiff = 25000;

    public CheckReach(Reflex reflex) {
        super(reflex, CheckType.REACH);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onReachDamage(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                final Player damager = (Player) event.getDamager();
                final Player player = (Player) event.getEntity();
                if (damager.getAllowFlight() || player.getAllowFlight()) return;
                final ReflexPlayer drp = getPlayer(damager);
                double reach = calculateReach(drp);
                if (reach != -1) {
                    if (player.getVelocity().length() > maxVelocityLength) return;
                    reach = calculatePingReach(drp, reach);
                    if (reach != -1) {
                        double distance = damager.getEyeLocation().distance(player.getLocation());
                        if (damager.getLocation().getY() > player.getLocation().getY()) {
                            double diff = damager.getLocation().getY() - player.getLocation().getY();
                            reach += diff / baseMaxReach;

                        } else if (player.getLocation().getY() > damager.getLocation().getY()) {
                            double diff = damager.getLocation().getY() - player.getLocation().getY();
                            reach += diff / baseMaxReach;
                        }

                        if (distance > reach) {

                            if (System.currentTimeMillis() - drp.getData().getLastReach() > maxTimeDiff) {
                                drp.getData().getReaches().clear();
                            }

                            drp.getData().getReaches().add(new ReachLog(distance, reach));
                            drp.getData().setLastReach(System.currentTimeMillis());

                            if (drp.getData().getReaches().size() >= minAttempts) {
                                double average = average(drp.getData().getReachDistancesAsDoubles());
                                double a = 6.0 - reach;
                                if (a < 0) {
                                    a = 0;
                                }
                                double b = average - reach;
                                int level = (int) Math.round(b / a * 100);
                                fail(drp, "avg: " + Math.round(average) + " [" + level + "%]")
                                        .cancelIfAllowed(event);
                                drp.getData().getReaches().clear(); // reset
                            }
                        }
                    }
                }
            }
        }
    }

    private double average(Set<Double> doubles) {
        double total = 0;
        for (double x : doubles) {
            total += x;
        }
        return total / doubles.size();
    }

    private double calculatePingReach(ReflexPlayer rp, double reach) {
        //Player player = rp.getBukkitPlayer();
        int ping = rp.getPing();
        if (ping >= 100 && ping < 200) {
            reach += 0.2;
        } else if (ping >= 200 && ping < 250) {
            reach += 0.4;
        } else if (ping >= 250 && ping < 300) {
            reach += 0.8;
        } else if (ping >= 300 && ping < 350) {
            reach += 1.2;
        } else if (ping >= 350 && ping < 400) {
            reach += 1.5;
        } else if (ping > 400) {
            return -1;
        }
        return reach;
    }

    private double calculateReach(ReflexPlayer rp) {
        Player player = rp.getBukkitPlayer();
        double reach = baseMaxReach;
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            int level = 0;
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.SPEED)) {
                    level = effect.getAmplifier();
                    break;
                }
            }
            switch (level) {
                case 0:
                    reach = 4.1;
                    break;
                case 1:
                    reach = 4.2;
                    break;
                case 2:
                    reach = 4.5;
                    break;
                default:
                    return -1;
            }
        }
        return reach;
    }

    public class ReachLog {

        private final double distance;
        private final double maxReach;

        public ReachLog(double distance, double maxReach) {
            this.distance = distance;
            this.maxReach = maxReach;
        }

        public double getDistance() {
            return distance;
        }

        public double getMaxReach() {
            return maxReach;
        }
    }


}
