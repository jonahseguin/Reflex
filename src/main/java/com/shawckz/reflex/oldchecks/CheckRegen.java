/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.oldchecks;


public class CheckRegen {

    /*

    @EventHandler
    public void onHealth(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player pl = (Player) e.getEntity();
            if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
                p.getData().setHealthPerSecond(p.getData().getHealthPerSecond() + e.getAmount());
            }
        }
    }

    @Override
    public void check(ReflexPlayer player) {
        if (player.getData().getHealthPerSecond() > 2) {
            fail(player);//Err how shall we cancel this
        }

        //Reset
        player.getData().setHealthPerSecond(0);
    }

*/

}
