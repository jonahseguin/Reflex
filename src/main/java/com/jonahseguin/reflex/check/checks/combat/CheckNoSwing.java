/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.combat;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.event.packet.ReflexPacketSwingEvent;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 16:56.
 * Project: Reflex
 */
public class CheckNoSwing extends Check {

    @ConfigData("ignore-tps")
    private double ignoreTps = 17.0;

    @ConfigData("check-delay")
    private long checkDelay = 10L;

    @ConfigData("max-swing-time-difference-milliseconds")
    private long maxTimeDiff = 1500L;

    @ConfigData("min-attempts")
    private int minAttempts = 3;

    public CheckNoSwing(Reflex reflex) {
        super(reflex, CheckType.NO_SWING);
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                if (Lag.getTPS() > ignoreTps) { // Ignore if too laggy because check will become inaccurate
                    final Player player = (Player) event.getDamager();
                    final ReflexPlayer rp = getPlayer(player);
                    if (rp.canCheck()) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!hasSwung(rp)) {
                                    rp.addPreVL(getCheckType());
                                    if (rp.getPreVL(getCheckType()) > minAttempts) {
                                        fail(rp, minAttempts + " attempts");
                                        rp.setPreVL(getCheckType(), 0); // Reset on fail
                                    }
                                }
                            }
                        }.runTaskLaterAsynchronously(getReflex(), checkDelay);
                    }
                }
            }
        }
    }

    private boolean hasSwung(ReflexPlayer reflexPlayer) {
        if (System.currentTimeMillis() < (reflexPlayer.getData().lastSwing + maxTimeDiff)) {
            reflexPlayer.getData().hasSwung = true;
            return true;
        }
        reflexPlayer.getData().hasSwung = false;
        return false;
    }

    @EventHandler
    public void onNoSwingPacketSwing(final ReflexPacketSwingEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer rp = getPlayer(player);

        rp.getData().hasSwung = true;
        rp.getData().lastSwing = System.currentTimeMillis();
    }

}
