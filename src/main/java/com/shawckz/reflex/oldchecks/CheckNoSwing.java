/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.oldchecks;

public class CheckNoSwing {

    /*

    public CheckNoSwing() {
        super(CheckType.NO_SWING);
    }

    @EventHandler
    public void onPlayerClick(PlayerAnimationEvent event) {
        ReflexCache.get().getReflexPlayer(event.getPlayer()).getData().setHasSwung(true);
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        ReflexPlayer reflexPlayer = ReflexCache.get().getReflexPlayer((Player) event.getDamager());
        if (!reflexPlayer.getData().isHasSwung()) {
            if (fail(reflexPlayer).isCancelled()) {
                event.setCancelled(true);
            }
        }
    }

    */

}
