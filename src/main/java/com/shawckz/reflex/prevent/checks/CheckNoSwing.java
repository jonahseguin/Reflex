/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.prevent.checks;

import com.shawckz.reflex.prevent.check.Check;
import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.player.ReflexCache;
import com.shawckz.reflex.core.player.ReflexPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;

public class CheckNoSwing extends Check {
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
}
