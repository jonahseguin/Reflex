/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.event.internal.ReflexVelocityEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@Setter
public class CheckSpeed extends RTrigger {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final double[] bhopValues = {0.313D, 0.612D, 0.36D, 0.353D, 0.347D, 0.341D, 0.336D, 0.331D, 0.327D, 0.323D, 0.32D, 0.316D};
    @ConfigData("player-movement-speed")
    private double movementSpeed = 0.287D;
    @ConfigData("default-bunny-hop")
    private double defaultBhop = 0.33D;

    @ConfigData("alert-threshold")
    private int alertThreshold = 3;

    @ConfigData("lag-threshold")
    private double lagThreshold = 10.0D;

    @ConfigData("max-movement-speed.speed-potion.amplifier")
    private double speedAmplifier = 0.125D;

    @ConfigData("max-movement-speed.speed-potion.multiplier")
    private double speedMultiplier = 2.5D;


    public CheckSpeed() {
        super(CheckType.SPEED, RCheckType.TRIGGER);
    }

    private double calculateMaxSpeed(Player p, Location from, Location to) {
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);

        double speed = movementSpeed * (p.getWalkSpeed() / 0.2D);

        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) {
                int amp = effect.getAmplifier() + 1;
                speed += speed * speedAmplifier * amp;
                speed *= speedMultiplier;
            }
        }

        if (rp.getData().underBlock(to)) {
            if (rp.getData().getHFreedom() < 1.7175D) {
                rp.getData().setHFreedom(1.7175D);
            }
        }
        if (rp.getData().onType(to, Material.ICE)) {
            if (rp.getData().getHFreedom() < 2.175D) {
                rp.getData().setHFreedom(2.175D);
            }
        }
        //For future versions: also check if on slime

        double deltaY = to.getY() - from.getY();

        if (deltaY > 0.0D && (deltaY < 0.6D) && (rp.getData().getHFreedom() < (deltaY * 1.3))) {
            rp.getData().setHFreedom(deltaY * 1.3D);
        }

        if (rp.getData().getHFreedom() > 10.0D) {
            rp.getData().setHFreedom(10.0D);
        }
        return speed;
    }

    private double recalculateFail(ReflexPlayer rp, double hDistance) {
        double bhop = getBunnyHop(rp);
        hDistance -= bhop;
        if (hDistance > 0.0D) {
            rp.getData().setHFreedom(rp.getData().getHFreedom() - (hDistance / 2.0D));
            if (rp.getData().getHFreedom() > 0.0D) {
                hDistance = 0.0D;
            }
        }
        return hDistance;
    }

    private double getBunnyHop(ReflexPlayer rp) {
        if (rp.getData().getBhopDelay() > (bhopValues.length - 1)) {
            return defaultBhop;
        }
        return bhopValues[rp.getData().getBhopDelay()];
    }

    @EventHandler
    public void onVelocity(ReflexVelocityEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
        double x = Math.abs(e.getX()) * 5.0D;
        double z = Math.abs(e.getZ()) * 5.0D;
        if (x + z > rp.getData().getHFreedom()) {
            rp.getData().setHFreedom(x + z);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!e.isCancelled()) {
            ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
            if (rp.getData().isOnGround(e.getTo())) {
                rp.getData().setBhopDelay(0);
            }
            else {
                rp.getData().setBhopDelay(rp.getData().getBhopDelay() + 1);
            }

            if (p.getVehicle() == null && !p.getAllowFlight()) {
                double maxSpeed = calculateMaxSpeed(p, e.getFrom(), e.getTo());
                double speed = rp.getData().getHDistance(e.getFrom(), e.getTo());

                if (speed > maxSpeed && speed < lagThreshold) { // < 10 in case of lag
                    final double beforeSpeed = speed;
                    speed = recalculateFail(rp, speed);


                    if (speed > 0.0D) {
                        //   p.sendMessage(ChatColor.DARK_RED + "FAIL - " + beforeSpeed + " > " + maxSpeed);
                        rp.addAlertVL(getCheckType());
                        if (rp.getAlertVL(getCheckType()) >= alertThreshold) {
                            if (fail(rp, (df.format(beforeSpeed)) + " m/s > " + df.format(maxSpeed) + " m/s").isCancelled()) {
                                e.setTo(e.getFrom());
                            }
                            rp.setAlertVL(getCheckType(), 0);
                        }
                    }
                }
            }

        }
    }


    @Override
    public int getCaptureTime() {
        return -1;
    }
}
