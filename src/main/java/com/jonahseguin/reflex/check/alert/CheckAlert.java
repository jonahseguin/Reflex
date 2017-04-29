/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.check.CheckViolation;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:52.
 * Project: Reflex
 */
@Getter
public class CheckAlert implements Alert {

    private final String id;
    private final CheckViolation violation;
    private final double tps;
    private final int ping;
    @Setter
    private String detail = "";
    private final AlertType ALERT_TYPE = AlertType.SINGLE;

    public CheckAlert(CheckViolation violation, double tps, int ping) {
        this.id = UUID.randomUUID().toString().toLowerCase();
        this.violation = violation;
        this.tps = tps;
        this.ping = ping;
    }

    public static void staffMsg(String msg) {
        for (Player pl : Reflex.getOnlinePlayers()) {
            ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
            if (p.isAlertsEnabled() && ReflexPerm.ALERTS.hasPerm(pl)) {
                p.msg(msg);
            }
        }
        Bukkit.getLogger().info(ChatColor.stripColor(msg));
    }

    public static void staffMsg(FancyMessage msg) {
        for (Player pl : Reflex.getOnlinePlayers()) {
            ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
            if (p.isAlertsEnabled() && ReflexPerm.ALERTS.hasPerm(pl)) {
                msg.send(pl);
            }
        }
        msg.send(Bukkit.getConsoleSender());
    }

    @Override
    public void sendAlert() {
        // Send (single) alert
        ReflexLang format = ReflexLang.ALERT_SINGLE; // player, check, ping, detail, vl
        String detail = this.detail;
        if (detail.equals("")) {
            detail = "n/a";
        }

        FancyMessage msg = new FancyMessage(RLang.format(ReflexLang.ALERT_PREFIX));
        String s = RLang.format(format, getReflexPlayer().getName(), getCheckType().getName(), getPing()+"", detail, getVl() + "");
        msg.then(s)
                .tooltip(
                        ChatColor.GRAY + "Click to view alert",
                        ChatColor.GRAY + "Player: " + ChatColor.RED + getReflexPlayer().getName(),
                        ChatColor.GRAY + "Check: " + ChatColor.RED + getCheckType().getName(),
                        ChatColor.GRAY + "Ping: " + ChatColor.RED + getPing() + ChatColor.GRAY + ", " +
                                "TPS: " + ChatColor.RED + getTps()
                )
                .command("/reflex alert " + id);

        staffMsg(msg);

        getReflexPlayer().setLastAlertTime(System.currentTimeMillis());

        Reflex.log("Alert: " + getReflexPlayer().getName() + " (" + getCheckType().getName() + ") [" + id + "]");
    }

    @Override
    public ReflexPlayer getReflexPlayer() {
        return violation.getReflexPlayer();
    }

    @Override
    public int getVl() {
        return violation.getVl();
    }

    @Override
    public CheckType getCheckType() {
        return violation.getCheckType();
    }

    @Override
    public AlertType getAlertType() {
        return ALERT_TYPE;
    }
}
