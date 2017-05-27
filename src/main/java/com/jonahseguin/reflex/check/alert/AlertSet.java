/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.google.common.collect.Sets;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:59.
 * Project: Reflex
 */
@Getter
public class AlertSet {

    private final ReflexPlayer reflexPlayer;
    private final CheckType checkType;
    private final Set<CheckAlert> alerts;
    private Map.Entry<Long, CheckAlert> mostRecentAlert = null;

    public AlertSet(ReflexPlayer reflexPlayer, CheckType checkType) {
        this.reflexPlayer = reflexPlayer;
        this.checkType = checkType;
        this.alerts = Sets.newHashSet();
    }

    public AlertSet(ReflexPlayer reflexPlayer, CheckType checkType, Set<CheckAlert> alerts, Map.Entry<Long, CheckAlert> mostRecentAlert) {
        this.reflexPlayer = reflexPlayer;
        this.checkType = checkType;
        this.alerts = alerts;
        this.mostRecentAlert = mostRecentAlert;
    }

    public void addAlert(CheckAlert alert) {
        Validate.isTrue(alert.getCheckType().equals(checkType), "AlertSet alerts must all be of same CheckType");
        this.mostRecentAlert = new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), alert);
        alerts.add(alert);
    }

    public void clearAlerts() {
        alerts.clear();
        mostRecentAlert = null;
    }

    public boolean shouldSendAsGrouped() {
        return alerts.size() > 1;
    }

    public AlertSet copy() {
        return new AlertSet(this.reflexPlayer, this.checkType, this.alerts, this.mostRecentAlert);
    }

    public int count() {
        return alerts.size();
    }

}
