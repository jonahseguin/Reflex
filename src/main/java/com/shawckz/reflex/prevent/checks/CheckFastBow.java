/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.prevent.checks;

import com.shawckz.reflex.prevent.check.Check;
import com.shawckz.reflex.prevent.check.CheckData;
import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.player.ReflexCache;
import com.shawckz.reflex.core.player.ReflexPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class CheckFastBow extends Check {

    public CheckFastBow(){
        super(CheckType.FAST_BOW);
    }

    @EventHandler
    public void onPull(PlayerInteractEvent e){
        Player pl = e.getPlayer();
        ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
        if(pl.getItemInHand() != null){
            if(pl.getItemInHand().getType() == Material.BOW){
                p.getData().setBowPull(System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent e){
        if(e.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) e.getEntity();
            if(arrow.getShooter() != null){
                if(arrow.getShooter() instanceof Player){
                    Player pl = (Player) arrow.getShooter();
                    ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
                    p.getData().setBowShoot(System.currentTimeMillis());
                    p.getData().setBowPower(e.getEntity().getVelocity().length());
                    if(check(p)){
                        e.setCancelled(true);
                    }
                    reset(p);
                }
            }
        }
    }

    private boolean check(ReflexPlayer p){
        CheckData data = p.getData();
        double pull = data.getBowShoot() - data.getBowPull();
        double power = data.getBowPower();

        if(power >= 2.5){
            if(pull <= 200){
                return fail(p).isCancelled();
            }
        }
        return false;
    }

    @EventHandler
    public void onSwap(PlayerItemHeldEvent e){
        Player pl = e.getPlayer();
        ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
        reset(p);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player pl = e.getPlayer();
        ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
        reset(p);
    }

    private void reset(ReflexPlayer p){
        p.getData().setBowShoot(0);
        p.getData().setBowPull(0);
        p.getData().setBowPower(0);
    }

}
