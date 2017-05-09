/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.other;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 17:02.
 * Project: Reflex
 */
public class CheckSelfHit extends Check {


    public CheckSelfHit(Reflex reflex) {
        super(reflex, CheckType.SELF_HIT);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageSelfHit(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final Player damager = (Player) event.getDamager();
            if (player.getName().equals(damager.getName())) {
                final ReflexPlayer rp = getPlayer(player);
                fail(rp).cancelIfAllowed(event);
            }
        }
    }

    /*
    @EventHandler
    public void onDamageSelfHit(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                final Player player = (Player) event.getEntity();
                final ReflexPlayer rp = getPlayer(player);
                List<Player> nearby = rp.getData().getNearby(20, 20, 20);
                if (nearby.isEmpty()) {

                }
            }
        }
    }
    */

}
