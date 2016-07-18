/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@Getter
public class TriggerCriticals extends RTrigger {

    @ConfigData("capture-time")
    private int captureTime = 30;

    //For trigger: just check if they get X amount of criticals in a row

    //For inspecting: compare time between criticals, if the Y / velocity they hit the player at is the same every time, consecutive criticals

    @ConfigData("min-consecutive-critical-hits")
    private int minCriticalHits = 10;

    @ConfigData("non-critical-hit-penalty")
    private double nonCriticalPenalty = 0.75;

    @ConfigData("max-total-y")
    private double maxTotalY = 20.0D;

    public TriggerCriticals() {
        super(CheckType.CRITICALS, RCheckType.TRIGGER);
    }

    private boolean canCrit(Player player) {
        Location loc = player.getLocation();

        if (!player.getAllowFlight() && !player.isFlying()) {
            if (player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE) {
                Location below = loc.clone().subtract(0, 0.025, 0);
                if (!below.getBlock().getType().isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
            if(canCrit(p)) {
                //Was critical hit
                rp.getData().setConsecutiveCriticalHits(rp.getData().getConsecutiveCriticalHits() + 1);

                if(rp.getData().getConsecutiveCriticalHits() >= minCriticalHits) {
                    if(rp.getData().getTotalCriticalY() <= maxTotalY) {
                        fail(rp, "" + Math.round(rp.getData().getTotalCriticalY()) + " total y");
                        rp.getData().setConsecutiveCriticalHits(0);
                    }
                    rp.getData().setTotalCriticalY(0);
                }

                rp.getData().setLastCriticalY(p.getLocation().getY());
            }
            else{
                //Not critical hit
                rp.getData().setConsecutiveCriticalHits(rp.getData().getConsecutiveHits() - nonCriticalPenalty);
                if(rp.getData().getConsecutiveCriticalHits() < 0) {
                    rp.getData().setConsecutiveCriticalHits(0);
                }
                rp.getData().setTotalCriticalY(rp.getData().getTotalCriticalY() * 0.75);
            }
          //  p.sendMessage(ChatColor.GOLD + "Critical hits: " + rp.getData().getConsecutiveCriticalHits());
          //  p.sendMessage(ChatColor.GRAY + "Total y: " + rp.getData().getTotalCriticalY());
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        if(e.isCancelled()) return;
        if(e.getTo().getY() == e.getFrom().getY()) return;
        Player p = e.getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);

        rp.getData().setTotalCriticalY(rp.getData().getTotalCriticalY() + (e.getTo().getY() > e.getFrom().getY() ? e.getTo().getY() - e.getFrom().getY() : e.getFrom().getY() - e.getTo().getY()));
    }

}
