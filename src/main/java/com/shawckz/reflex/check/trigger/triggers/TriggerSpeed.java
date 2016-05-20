/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.RTrigger;
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
public class TriggerSpeed extends RTrigger implements RTimer {

    @ConfigData("capture-time")
    private int captureTime = 10;

    @ConfigData("max-blocks-per-second")
    private double maxBlocksPerSecond = 18.5;

    public TriggerSpeed() {
        super(CheckType.SPEED, RCheckType.TRIGGER);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if(p.getAllowFlight() == true || p.isFlying() || p.getGameMode() == GameMode.CREATIVE) return;
        ReflexPlayer ap = Reflex.getInstance().getCache().getReflexPlayer(p);
        final double xDistance = e.getTo().getX() - e.getFrom().getX();
        final double zDistance = e.getTo().getZ() - e.getFrom().getZ();

        double distance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);

        //HORIZONTAL DISTANCE
        PotionEffect potionEffect = null;
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) {
                potionEffect = effect;
                break;
            }
        }
        if (potionEffect != null) {
            //They have speed
            int amp = potionEffect.getAmplifier();
            distance -= distance * (amp * 0.2);
        }
        if(distance > 0) {
            ap.getData().setBlocksPerSecond(ap.getData().getBlocksPerSecond() + distance);
        }
    }

    @Override
    public void runTimer() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            ReflexPlayer player = Reflex.getInstance().getCache().getReflexPlayer(pl);

            if(player.getData().getBlocksPerSecond() > maxBlocksPerSecond) {
                triggerLater(player, result -> {

                });
            }

            player.getData().setBlocksPerSecond(0);

        }
    }
}
