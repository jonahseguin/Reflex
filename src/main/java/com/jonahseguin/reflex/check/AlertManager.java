/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.oldchecks.base.RTimer;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:54.
 * Project: Reflex
 */
public class AlertManager implements RTimer {

    public static final int MAX_ALERTS_PER_PLAYER_PER_SECOND = 3;

    private final ConcurrentLinkedQueue<AlertGroup> alertQueue = new ConcurrentLinkedQueue<>();

    public AlertManager() {
    }

    @Override
    public void runTimer() {
        //TODO CHANGE: WE ONLY WANT THIS TO RUN EVERY 3 SECONDS
        // WE WANT ALERTS TO BUILD UP, GROUP THEM, AND THEN SEND AS A GROUP
        // once per second
        for (Player pl : Reflex.getOnlinePlayers()) {
            if(alertQueue.isEmpty()) {
                break;
            }
            for (int i = 0; i < MAX_ALERTS_PER_PLAYER_PER_SECOND; i++) {
                if(alertQueue.isEmpty()) {
                    break;
                }
                AlertGroup alertGroup = alertQueue.poll();
                if (alertGroup != null) {
                    if (alertGroup.shouldSendAsGrouped()) {
                        GroupedAlert groupedAlert = createGroupedAlert(alertGroup);

                        alertGroup.clearAlerts(); // Clear the alerts since we've now dealt with them for this check
                    }
                }
            }
        }
    }

    public GroupedAlert createGroupedAlert(AlertGroup alertGroup) {
        return new GroupedAlert(alertGroup.copy()); // Make sure we use the .copy() to preserve data
    }




}
