/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.movement;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:59.
 * Project: Reflex
 * <p>
 * Hack Description: Prevents a player from taking knockback when being attacked
 * Check Description: Ensures a player does not remain in the same location after being attacked
 */
public class CheckAntiKnockback extends Check {

    @ConfigData("max-distance-offset")
    private double maxDistanceOffset = 0.01D;

    @ConfigData("minimum-attempts")
    private int minAttempts = 3;

    public CheckAntiKnockback(Reflex instance) {
        super(instance, CheckType.ANTI_KNOCKBACK);
    }

    @EventHandler
    public void onAntiKnockbackVelocity(final PlayerVelocityEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);
        if (!rp.canCheck()) return;
        if (player.isInsideVehicle()) return;
        if (player.hasPotionEffect(PotionEffectType.WITHER) || player.hasPotionEffect(PotionEffectType.POISON)) return;
        if (player.getLocation().getBlock().getLocation().add(0, 2, 0).getBlock().getType() == null) return;
        if (!player.getLocation().getBlock().getLocation().add(0, 2, 0).getBlock().getType().equals(Material.AIR))
            return;
        final Location damageLocation = player.getLocation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isValid() || !player.isOnline()) return;
                if (player.getLocation().distanceSquared(damageLocation) <= maxDistanceOffset) {
                    rp.addAlertVL(getCheckType());
                    if (rp.getAlertVL(getCheckType()) >= minAttempts) {
                        fail(rp);
                        rp.setAlertVL(getCheckType(), 0); // Reset on fail
                    }
                } else {
                    rp.setAlertVL(getCheckType(), 0);
                }
            }
        }.runTaskLater(getReflex(), 10L);

    }


}
