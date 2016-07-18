/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.event.internal.ReflexSwingEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CheckNoSwing extends RTrigger {

    @ConfigData("swing-vl-penalty") //VL that is subtracted when they actually swing
    private int vlPenalty = 3;

    @ConfigData("max-consecutive-noswings")
    private int maxNoSwings = 10;

    public CheckNoSwing() {
        super(CheckType.NO_SWING, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onSwing(ReflexSwingEvent e) {
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
        rp.getData().setHasSwung(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAttack(EntityDamageByEntityEvent e) {
        if(e.isCancelled()) return;
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
            if(rp.getData().isHasSwung()) {
                rp.getData().setHasSwung(false);
                if(rp.getAlertVL(getCheckType()) >= vlPenalty) {
                    rp.modifyAlertVL(getCheckType(), (vlPenalty * -1));
                }
                else{
                    rp.setAlertVL(getCheckType(), 0);
                }
            }
            else{
                rp.addAlertVL(getCheckType());

                if(rp.getAlertVL(getCheckType()) >= maxNoSwings) {
                    fail(rp, rp.getAlertVL(getCheckType()) + " swings");
                    rp.setAlertVL(getCheckType(), 0);
                }

            }
        }
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }

}
