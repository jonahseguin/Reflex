package com.shawckz.reflex.checks;

import com.shawckz.reflex.check.Check;
import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;

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
            ReflexPlayer ap = ReflexCache.get().getAresPlayer(p);

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
