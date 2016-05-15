/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.oldchecks;


public class CheckReach {

    /*

    public CheckReach() {
        super(CheckType.REACH);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.THORNS) return;
        if (e.isCancelled()) return;
        if (e.getDamager() instanceof Player) {
            final Player p = (Player) e.getDamager();
            ReflexPlayer ap = ReflexCache.get().getReflexPlayer(p);

            Location dmger = p.getLocation().add(0, 1.5, 0);
            Location dmged = e.getEntity().getLocation();

            double distance = dmger.distance(dmged);

            if (distance > 10.5) {
                if (fail(ap).isCancelled()) {
                    e.setCancelled(true);
                }
            }

        }
    }

*/

}
