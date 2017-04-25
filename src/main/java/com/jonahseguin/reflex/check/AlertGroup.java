/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:59.
 * Project: Reflex
 */
@Getter
public class AlertGroup {

    private final ReflexPlayer reflexPlayer;
    private final CheckType checkType;
    private final Set<CheckAlert> alerts = new HashSet<>();
    private long mostRecentAlert = 0;

    public AlertGroup(ReflexPlayer reflexPlayer, CheckType checkType) {
        this.reflexPlayer = reflexPlayer;
        this.checkType = checkType;
    }

    public void addAlert(CheckAlert alert) {
        this.mostRecentAlert = alert.getViolation().getTime();
        alerts.add(alert);
    }

    public void clearAlerts() {
        alerts.clear();
    }
}
