/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.prevent.checks;

import com.shawckz.reflex.prevent.check.Check;
import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.player.ReflexCache;
import com.shawckz.reflex.core.player.ReflexPlayer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CheckReach extends Check {

    public CheckReach() {
        super(CheckType.REACH);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getCause() == EntityDamageEvent.DamageCause.THORNS) return;
        if(e.isCancelled()) return;
        if(e.getDamager() instanceof Player){
            final Player p = (Player) e.getDamager();
            ReflexPlayer ap = ReflexCache.get().getReflexPlayer(p);

            Location dmger = p.getLocation().add(0, 1.5, 0);
            Location dmged = e.getEntity().getLocation();

            double distance = dmger.distance(dmged);

            if(distance > 10.5){
                if(fail(ap).isCancelled()){
                    e.setCancelled(true);
                }
            }

        }
    }

}
