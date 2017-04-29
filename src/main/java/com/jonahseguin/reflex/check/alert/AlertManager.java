/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.google.common.collect.Sets;
import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.check.CheckViolation;
import com.jonahseguin.reflex.oldchecks.base.RTimer;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;

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

    @Override
    public void runTimer() {
        //TODO CHANGE: WE ONLY WANT THIS TO RUN EVERY 3 SECONDS
        // WE WANT ALERTS TO BUILD UP, GROUP THEM, AND THEN SEND AS A GROUP
        // once per second
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
                                CheckAlert checkAlert = alertSet.getMostRecentAlert().getValue();
                                checkAlert.sendAlert();
                            }
                            alertSet.clearAlerts();
                        }
                        x++;
                    }
                }
            }
        }
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
     * @param violation CheckViolation, violation to alert for
     * @return Alert --> created alert, is also cached and handled by this method
     */
    public Alert alert(CheckViolation violation) {
        CheckAlert alert = new CheckAlert(violation, Lag.getTPS(), violation.getReflexPlayer().getPing());

        cacheAlert(alert);

        violation.getReflexPlayer().getAlerts().getAlertGroup(violation.getCheckType()).addAlert(alert);

        return alert;
    }

    public GroupedAlert createGroupedAlert(AlertSet alertSet) {
        return new GroupedAlert(alertSet.copy()); // Make sure we use the .copy() to preserve data
    }


}
