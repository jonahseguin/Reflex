/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.obj;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RViolation;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResult;
import com.jonahseguin.reflex.util.utility.ReflexException;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import mkremins.fanciful.FancyMessage;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
public class Alert {

    private final String id;
    private final ReflexPlayer violator;
    private final RViolation violation;
    private final long time;
    private final Type type;
    private final CheckType checkType;
    private final double tps;
    private final int vl;
    @Setter
    private String detail = null;
    @Setter
    private RInspectResult inspectResult = null;

    public Alert(ReflexPlayer violator, CheckType checkType, Type type, RViolation violation, int vl) {
        this.violator = violator;
        this.checkType = checkType;
        this.violation = violation;
        this.type = type;
        this.id = UUID.randomUUID().toString();
        this.time = System.currentTimeMillis();
        this.tps = Lag.getTPS();
        this.vl = vl;
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

    public void sendAlert() {
        FancyMessage message = new FancyMessage(RLang.format(ReflexLang.ALERT_PREFIX));

        if (type == Type.INSPECT_FAIL) {
            if (inspectResult == null) {
                throw new ReflexException("Cannot send inspect fail alert because inspectResult is null");
            }
            String format;
            if (detail != null) {
                format = RLang.format(ReflexLang.ALERT_INSPECT_DETAIL, violator.getName(), checkType.getName(), detail, vl + "");
            }
            else {
                format = RLang.format(ReflexLang.ALERT_INSPECT, violator.getName(), checkType.getName(), vl + "");
            }
            message.then(format)
                    .tooltip(ChatColor.translateAlternateColorCodes('&', "&7[Inspect &cFail&7] &eClick for more information"))
                    .command("/reflex lookup inspection " + inspectResult.getId());
        }
        else if (type == Type.INSPECT_PASS) {
            if (inspectResult == null) {
                throw new ReflexException("Cannot send inspect pass alert because inspectResult is null");
            }
            String format;
            if (detail != null) {
                format = RLang.format(ReflexLang.ALERT_INSPECT_PASS_DETAIL, violator.getName(), checkType.getName(), detail);
            }
            else {
                format = RLang.format(ReflexLang.ALERT_INSPECT_PASS, violator.getName(), checkType.getName());
            }
            message.then(format)
                    .tooltip(ChatColor.translateAlternateColorCodes('&', "&7[Inspect &aPass&7] &eClick for more information"))
                    .command("/reflex lookup inspection " + inspectResult.getId());
        }
        else if (type == Type.TRIGGER) {
            message.then(RLang.format(ReflexLang.ALERT_TRIGGER, violator.getName(), checkType.getName()));
        }
        else if (type == Type.FAIL) {
            if (detail == null) {
                message.then(RLang.format(ReflexLang.ALERT_FAIL, violator.getName(), checkType.getName(), vl + ""))
                        .tooltip(ChatColor.translateAlternateColorCodes('&', "&7[Fail] &eClick for more information"))
                        .command("/reflex lookup violation " + violation.getId());
            }
            else {
                message.then(RLang.format(ReflexLang.ALERT_FAIL_DETAIL, violator.getName(), checkType.getName(), detail, vl + ""))
                        .tooltip(ChatColor.translateAlternateColorCodes('&', "&7[Fail] &eClick for more information"))
                        .command("/reflex lookup violation " + violation.getId());
            }
        }
        else {
            throw new ReflexException("Unknown Alert Type " + type.toString());
        }
        staffMsg(message);
    }

    public enum Type {
        TRIGGER,
        INSPECT_FAIL,
        INSPECT_PASS,
        FAIL
    }

}
