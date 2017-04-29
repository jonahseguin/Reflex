/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.Map;
import java.util.Set;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:59.
 * Project: Reflex
 */
@Getter
public class AlertGroup {

    private final ReflexPlayer reflexPlayer;
    private final CheckType checkType;
    private final Set<CheckAlert> alerts;
    private Map.Entry<Long, CheckAlert> mostRecentAlert = null;

    public AlertGroup(ReflexPlayer reflexPlayer, CheckType checkType) {
        this.reflexPlayer = reflexPlayer;
        this.checkType = checkType;
        this.alerts = Sets.newHashSet();
    }

    public AlertGroup(ReflexPlayer reflexPlayer, CheckType checkType, Set<CheckAlert> alerts, Map.Entry<Long, CheckAlert> mostRecentAlert) {
        this.reflexPlayer = reflexPlayer;
        this.checkType = checkType;
        this.alerts = alerts;
        this.mostRecentAlert = mostRecentAlert;
    }

    public void addAlert(CheckAlert alert) {
        Validate.isTrue(alert.getCheckType().equals(checkType), "AlertGroup alerts must all be of same CheckType");
        this.mostRecentAlert = Maps.immutableEntry(System.currentTimeMillis(), alert);
        alerts.add(alert);
    }

    public void clearAlerts() {
        alerts.clear();
    }

    public boolean shouldSendAsGrouped() {
        return alerts.size() > 1;
    }

    public AlertGroup copy() {
        return new AlertGroup(reflexPlayer, checkType, alerts, mostRecentAlert);
    }

}
