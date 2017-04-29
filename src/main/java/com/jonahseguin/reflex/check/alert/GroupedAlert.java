/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * Created by Jonah Seguin on Fri 2017-04-28 at 01:27.
 * Project: Reflex
 */
public class GroupedAlert implements Alert {

    private final ReflexPlayer reflexPlayer;
    private final AlertSet alertSet;
    private final int vl;
    private final String id;
    private final AlertType ALERT_TYPE = AlertType.GROUPED;

    public GroupedAlert(AlertSet alertSet) {
        this.reflexPlayer = alertSet.getReflexPlayer();
        this.alertSet = alertSet;
        this.vl = alertSet.getMostRecentAlert().getValue().getVl();
        this.id = UUID.randomUUID().toString().toLowerCase();
    }

    @Override
    public void sendAlert() {
        // Send (single) alert
        ReflexLang format = ReflexLang.ALERT_SINGLE; // player, check, grouped count, vl

        FancyMessage msg = new FancyMessage(RLang.format(ReflexLang.ALERT_PREFIX));
        String s = RLang.format(format, getReflexPlayer().getName(), getCheckType().getName(), alertSet.count()+"", vl+"");
        msg.then(s)
                .tooltip(
                        ChatColor.GRAY + "Click to view alert",
                        ChatColor.GRAY + "[MULTIPLE VIOLATIONS] (" + alertSet.count() + "x)",
                        ChatColor.GRAY + "Player: " + ChatColor.RED + getReflexPlayer().getName(),
                        ChatColor.GRAY + "Check: " + ChatColor.RED + getCheckType().getName()
                )
                .command("/reflex alert " + id);

        CheckAlert.staffMsg(msg);

        getReflexPlayer().setLastAlertTime(System.currentTimeMillis());

        Reflex.log("Alert [MULTIPLE]: " + getReflexPlayer().getName() + " (" + getCheckType().getName() + ") [" + id + "]");

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ReflexPlayer getReflexPlayer() {
        return reflexPlayer;
    }

    @Override
    public String getDetail() {
        return alertSet.getMostRecentAlert().getValue().getDetail();
    }

    @Override
    public int getVl() {
        return vl;
    }

    @Override
    public CheckType getCheckType() {
        return alertSet.getCheckType();
    }

    @Override
    public AlertType getAlertType() {
        return ALERT_TYPE;
    }
}
