/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.PlayerData;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

@Getter
@Setter
public class TriggerFastBow extends RTrigger {

    @ConfigData("minimum-power")
    private double minPower = 2.5;

    @ConfigData("pull-shoot-threshold-milliseconds")
    private double maxPull = 200; //Difference between pull and shoot in milliseconds

    @ConfigData("capture-time")
    private int captureTime = 15;

    public TriggerFastBow() {
        super(CheckType.FAST_BOW, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onPull(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
        if (pl.getItemInHand() != null) {
            if (pl.getItemInHand().getType() == Material.BOW) {
                p.getData().setBowPull(System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();
            if (arrow.getShooter() != null) {
                if (arrow.getShooter() instanceof Player) {
                    Player pl = (Player) arrow.getShooter();
                    ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
                    p.getData().setBowShoot(System.currentTimeMillis());
                    p.getData().setBowPower(e.getEntity().getVelocity().length());
                    if (check(p)) {
                        e.setCancelled(true);
                    }
                    reset(p);
                }
            }
        }
    }

    private boolean check(ReflexPlayer p) {
        PlayerData data = p.getData();
        double pull = data.getBowShoot() - data.getBowPull();
        double power = data.getBowPower();

        if (power >= minPower) {
            if (pull <= maxPull) {
                return trigger(p).isCancelled();
            }
        }
        return false;
    }

    @EventHandler
    public void onSwap(PlayerItemHeldEvent e) {
        Player pl = e.getPlayer();
        ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
        reset(p);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player pl = e.getPlayer();
        ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
        reset(p);
    }

    private void reset(ReflexPlayer p) {
        p.getData().setBowShoot(0);
        p.getData().setBowPull(0);
        p.getData().setBowPower(0);
    }

}
