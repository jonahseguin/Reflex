package com.shawckz.reflex.checks;

import com.shawckz.reflex.check.Check;
import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;

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
        ReflexCache.get().getAresPlayer(event.getPlayer()).getData().setHasSwung(true);
    }
    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        ReflexPlayer reflexPlayer = ReflexCache.get().getAresPlayer((Player) event.getDamager());
        if (!reflexPlayer.getData().isHasSwung()) {
            if (fail(reflexPlayer).isCancelled()) {
                event.setCancelled(true);
            }
        }
    }
}
