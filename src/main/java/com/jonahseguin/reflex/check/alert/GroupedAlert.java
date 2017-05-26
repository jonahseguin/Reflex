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
        // &9PLAYER &7failed &cCHECK &7(COUNT&7) &7[VIOLATIONS&7VL]
        FancyMessage msg = new FancyMessage(RLang.format(ReflexLang.ALERT_PREFIX));

        final String player = reflexPlayer.getName();
        final String check = getCheckType().getName();
        final String count = alertSet.count() + "";
        final String violations = vl + "";
        final String command = "/reflex alert " + id;

        // Player
        msg
                .then(player)
                .color(ChatColor.BLUE)
                .tooltip(
                        ChatColor.GRAY + "[Player]",
                        ChatColor.GRAY + "Username: " + ChatColor.RED + player,
                        ChatColor.GRAY + "" + reflexPlayer.getSessionVL() + "sVL, "
                                + reflexPlayer.getPing() + "ms",
                        "  ",
                        ChatColor.GRAY + "[" + ChatColor.GREEN + "CLICK TO VIEW" + ChatColor.GRAY + "]"
                )
                .command(command);


        msg.then(" failed ").color(ChatColor.GRAY).command(command);

        // Check
        msg
                .then(check)
                .color(ChatColor.RED)
                .tooltip(
                        ChatColor.GRAY + "[Check]",
                        ChatColor.GRAY + "Check: " + ChatColor.RED + check,
                        ChatColor.GRAY + "Recent Detail: " + ChatColor.RED + getDetail(),
                        "  ",
                        ChatColor.GRAY + "[" + ChatColor.GREEN + "CLICK TO VIEW" + ChatColor.GRAY + "]"
                )
                .command(command);

        msg.then(" "); // Space

        msg
                .then("(" + count + "x)")
                .color(ChatColor.GRAY)
                .tooltip(
                        ChatColor.GRAY + "[Count]",
                        ChatColor.GRAY + "Count: " + ChatColor.RED + count,
                        "  ",
                        ChatColor.GRAY + "[" + ChatColor.GREEN + "CLICK TO VIEW" + ChatColor.GRAY + "]"
                )
                .command(command);

        msg.then(" "); // Space

        // Violation
        msg
                .then("[" + violations + "VL]")
                .color(ChatColor.GRAY)
                .tooltip(
                        ChatColor.GRAY + "[Violation] (" + alertSet.count() + "x)",
                        ChatColor.GRAY + "Player: " + ChatColor.RED + getReflexPlayer().getName(),
                        ChatColor.GRAY + "Check: " + ChatColor.RED + getCheckType().getName(),
                        "  ",
                        ChatColor.GRAY + "[" + ChatColor.GREEN + "CLICK TO VIEW" + ChatColor.GRAY + "]"
                )
                .command(command);

        AlertManager.staffMsg(msg);

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

    public AlertSet getAlertSet() {
        return alertSet;
    }
}
