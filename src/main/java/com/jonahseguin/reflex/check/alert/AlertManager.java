/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.oldchecks.base.RTimer;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import java.util.Iterator;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:54.
 * Project: Reflex
 */
public class AlertManager implements RTimer {

    private final int ALERT_BUILDUP_SECONDS; // time in seconds for alerts to group together before being sent
    private final int MAX_ALERTS; // per-player per-second

    private final Reflex instance;

    public AlertManager(Reflex instance) {
        this.instance = instance;
        this.ALERT_BUILDUP_SECONDS = instance.getReflexConfig().getAlertGroupingIntervalSeconds();
        this.MAX_ALERTS = instance.getReflexConfig().getMaxAlertsPPPS();
    }

    @Override
    public void runTimer() {
        //TODO CHANGE: WE ONLY WANT THIS TO RUN EVERY 3 SECONDS
        // WE WANT ALERTS TO BUILD UP, GROUP THEM, AND THEN SEND AS A GROUP
        // once per second
        for (ReflexPlayer p : instance.getCache().getOnlineReflexPlayers()) {
            if ((System.currentTimeMillis() - p.getLastAlertTime()) >= (1000 * ALERT_BUILDUP_SECONDS)) {
                // Has been at least 5 seconds since last alert for this player
                int x = 0;
                Iterator<AlertGroup> it = p.getAlerts().iterator();
                while (it.hasNext()) {
                    if (x >= MAX_ALERTS) {
                        break;
                    }
                    AlertGroup alertGroup = it.next();
                    if (alertGroup != null) {
                        if (alertGroup.shouldSendAsGrouped()) {
                            GroupedAlert groupedAlert = createGroupedAlert(alertGroup);
                            groupedAlert.sendAlert();

                            alertGroup.clearAlerts(); // Clear the alerts since we've now dealt with them for this check
                        } else {
                            CheckAlert checkAlert = alertGroup.getMostRecentAlert().getValue();
                            checkAlert.sendAlert();
                        }
                    }
                    p.getAlerts().remove(alertGroup);
                    x++;
                }
            }
        }
    }

    public GroupedAlert createGroupedAlert(AlertGroup alertGroup) {
        return new GroupedAlert(alertGroup.copy()); // Make sure we use the .copy() to preserve data
    }


}
