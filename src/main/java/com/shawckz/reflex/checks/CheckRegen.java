package com.shawckz.reflex.checks;


import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.check.TimerCheck;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class CheckRegen extends TimerCheck {

    public CheckRegen() {
        super(CheckType.REGEN);
    }

    @EventHandler
    public void onHealth(EntityRegainHealthEvent e){
        if(e.getEntity() instanceof Player){
            Player pl = (Player) e.getEntity();
            if(e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                ReflexPlayer p = ReflexCache.get().getAresPlayer(pl);
                p.getData().setHealthPerSecond(p.getData().getHealthPerSecond() + e.getAmount());
            }
        }
    }

    @Override
    public void check(ReflexPlayer player) {
        if(player.getData().getHealthPerSecond() > 2){
            fail(player);//Err how shall we cancel this
        }

        //Reset
        player.getData().setHealthPerSecond(0);
    }

}
