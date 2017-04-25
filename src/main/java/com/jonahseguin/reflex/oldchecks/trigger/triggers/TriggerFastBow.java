/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.triggers;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.data.PlayerData;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
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

    public TriggerFastBow(Reflex instance) {
        super(instance, CheckType.FAST_BOW, RCheckType.TRIGGER);
    }

    @EventHandler
    public void onPull(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        ReflexPlayer p = getPlayer(pl);
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
                    ReflexPlayer p = getPlayer(pl);
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
        ReflexPlayer p = getPlayer(pl);
        reset(p);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player pl = e.getPlayer();
        ReflexPlayer p = getPlayer(pl);
        reset(p);
    }

    private void reset(ReflexPlayer p) {
        p.getData().setBowShoot(0);
        p.getData().setBowPull(0);
        p.getData().setBowPower(0);
    }

}
