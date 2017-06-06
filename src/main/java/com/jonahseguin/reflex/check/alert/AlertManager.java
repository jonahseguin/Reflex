/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.google.common.collect.Sets;
import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.check.RTimer;
import com.jonahseguin.reflex.check.violation.CheckViolation;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:54.
 * Project: Reflex
 */
public class AlertManager implements RTimer {

    private final int ALERT_BUILDUP_SECONDS; // time in seconds for alerts to group together before being sent
    private final int MAX_ALERTS; // per-player per-second
    private final Reflex instance;

    private final Set<Alert> cache = Sets.newHashSet();

    public AlertManager(Reflex instance) {
        this.instance = instance;
        this.ALERT_BUILDUP_SECONDS = instance.getReflexConfig().getAlertGroupingIntervalSeconds();
        this.MAX_ALERTS = instance.getReflexConfig().getMaxAlertsPPPS();

        this.instance.getReflexTimer().registerTimer(this);
    }

    public static void staffMsg(String msg) {
        for (Player pl : Reflex.getOnlinePlayers()) {
            ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
            if (p.isAlertsEnabled() && ReflexPerm.ALERTS.hasPerm(pl)) {
                p.msg(msg);
            }
        }
        Reflex.getReflexLogger().info(ChatColor.stripColor(msg));
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
    public void runTimer() {
        for (ReflexPlayer p : instance.getCache().getOnlineReflexPlayers()) {
            if (!p.getAlerts().isEmpty()) {
                if ((System.currentTimeMillis() - p.getLastAlertTime()) >= (1000 * ALERT_BUILDUP_SECONDS)) {
                    // Has been at least 5 seconds since last alert for this player
                    int x = 0;
                    Iterator<CheckType> it = p.getAlerts().getAlertGroups().keySet().iterator();
                    while (it.hasNext()) {
                        if (x >= MAX_ALERTS) {
                            break;
                        }
                        CheckType checkType = it.next();
                        AlertSet alertSet = p.getAlerts().getAlertGroup(checkType);
                        if (alertSet != null) {
                            if (alertSet.shouldSendAsGrouped()) {
                                GroupedAlert groupedAlert = createGroupedAlert(alertSet);
                                groupedAlert.sendAlert();

                            } else {
                                if (alertSet.getMostRecentAlert() != null) {
                                    CheckAlert checkAlert = alertSet.getMostRecentAlert().getValue();
                                    checkAlert.sendAlert();
                                }
                            }
                            //alertSet.clearAlerts();
                            p.getAlerts().removeAlertGroup(checkType);
                            x++;
                        }
                    }
                }
            }
        }
    }

    public Alert getAlert(String id) {
        for (Alert alert : cache) {
            if (alert.getId().equalsIgnoreCase(id)) {
                return alert;
            }
        }
        return null;
    }

    public Alert cacheAlert(Alert alert) {
        cache.add(alert);
        return alert;
    }

    public Alert uncacheAlert(Alert alert) {
        cache.remove(alert);
        return alert;
    }

    /**
     * Will send an alert for the player for the provided check with provided detail
     * This method will not update VL, so the caller of this method is responsible for VL
     *
     * @param violation CheckViolation, infraction to alert for
     * @return Alert --> created alert, is also cached and handled by this method
     */
    public Alert alert(CheckViolation violation) {
        CheckAlert alert = new CheckAlert(violation, Lag.getTPS(), violation.getReflexPlayer().getPing());
        alert.setDetail(violation.getDetail());
        cacheAlert(alert);
        violation.getReflexPlayer().getAlerts().getAlertGroup(violation.getCheckType()).addAlert(alert);
        return alert;
    }

    public GroupedAlert createGroupedAlert(AlertSet alertSet) {
        alertSet = alertSet.copy();
        GroupedAlert groupedAlert = new GroupedAlert(alertSet); // Make sure we use the .copy() to preserve data
        cacheAlert(groupedAlert);
        return groupedAlert;
    }

}
