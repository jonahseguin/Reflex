/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

@Getter
@Setter
public class TriggerFly extends RTrigger implements RTimer{

    @ConfigData("capture-time")
    private int captureTime = 10;

    //AirTime
    @ConfigData("trigger-air-time")
    private int triggerAirTime = 5;

    @ConfigData("threshold-air-time")
    private int thresholdAirTime = 15;

    //FallVelocity
    @ConfigData("fall-velocity-trigger-air-time")
    private double fallVelocityAirTime = 3;

    public TriggerFly() {
        super(CheckType.FLY, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onUpdateGroundTime(PlayerMoveEvent e) {
        if(!isEnabled()) return;
        if(e.getPlayer().getAllowFlight()) return;
        if(e.getPlayer().getVehicle() != null) return;

        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(e.getPlayer());

        updateGroundTime(e.getPlayer(), e.getTo());


        if(e.getTo().getY() >= e.getFrom().getY()) {
            rp.getData().setHasPositiveVelocity(true);
        }
        else{
            rp.getData().setHasPositiveVelocity(false);
        }


    }
    private boolean canStandOn(Block b) {
        return b.getType().isSolid() || b.isLiquid();
    }

    private boolean updateGroundTime(Player p, Location to) {
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
        final int maxHeight = 2;

        if(p.getAllowFlight()) {
            rp.getData().setLastGroundtime(System.currentTimeMillis());
            return true;
        }

        boolean closeToGround = false;

        Location loc = to.clone().subtract(0, 1, 0);

        for (int x = 0; x < maxHeight; x++) {
            if (canStandOn(loc.getBlock())) {
                closeToGround = true;
                break;
            }
            if (x > maxHeight) {
                closeToGround = false;
                break;
            }
            loc = loc.subtract(0, 1, 0);
        }

        if(p.isSneaking()) {
                  /*
                1 2 3
                4 X 5
                6 7 8
                 */
            Location base = to.getBlock().getLocation();
            Location b1 = base.clone().add(1, 0 ,0);
            Location b2 = base.clone().add(0, 0 ,1);
            Location b3 = base.clone().add(-1, 0 ,0);
            Location b4 = base.clone().add(0, 0 ,-1);
            Location b5 = base.clone().add(1, 0 ,1);
            Location b6 = base.clone().add(-1, 0 ,-1);
            Location b7 = base.clone().add(1, 0 ,-1);
            Location b8 = base.clone().add(-1, 0 ,1);

            Set<Location> surrounding = new HashSet<>();
            surrounding.add(b1);
            surrounding.add(b2);
            surrounding.add(b3);
            surrounding.add(b4);
            surrounding.add(b5);
            surrounding.add(b6);
            surrounding.add(b7);
            surrounding.add(b8);

            Location closest = base;

            double dis = base.distance(p.getLocation());

            for(Location l : surrounding) {
                double d = l.distance(p.getLocation());
                if(d <= dis) {
                    closest = l;
                    dis = d;
                }
            }

            if(canStandOn(closest.getBlock()) || canStandOn(closest.clone().subtract(0,1,0).getBlock())) {
                closeToGround = true;
            }

        }

        if (closeToGround) {
            rp.getData().setLastGroundtime(System.currentTimeMillis());
        }

        return closeToGround;
    }


    @Override
    public void runTimer() {
        if(!isEnabled()) return;
        for(Player pl : Bukkit.getOnlinePlayers()) {
            ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(pl);

            updateGroundTime(pl, pl.getLocation());

            if(rp.getData().getLastGroundtime() != -1 && rp.getData().getLastGroundtime() < System.currentTimeMillis()) {
                int airTime = Math.round((System.currentTimeMillis() - rp.getData().getLastGroundtime()) / 1000);
                if(rp.getData().isHasPositiveVelocity() && airTime >= triggerAirTime) {
                    if(airTime >= thresholdAirTime) {
                        fail(rp, airTime + "s in air");
                        rp.getData().setLastGroundtime(System.currentTimeMillis());
                    }
                    else{
                        triggerLater(rp, result -> {});
                    }
                }
            }
        }
    }
}
