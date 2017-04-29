/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import java.util.UUID;

/**
 * Created by Jonah Seguin on Fri 2017-04-28 at 01:27.
 * Project: Reflex
 */
public class GroupedAlert implements Alert {

    private final ReflexPlayer reflexPlayer;
    private final AlertGroup alertGroup;
    private final int vl;
    private final String id;
    private final AlertType ALERT_TYPE = AlertType.GROUPED;

    public GroupedAlert(AlertGroup alertGroup) {
        this.reflexPlayer = alertGroup.getReflexPlayer();
        this.alertGroup = alertGroup;
        this.vl = alertGroup.getMostRecentAlert().getValue().getVl();
        this.id = UUID.randomUUID().toString().toLowerCase();
    }

    @Override
    public void sendAlert() {
        //TODO: Send grouped alert
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
        return alertGroup.getMostRecentAlert().getValue().getDetail();
    }

    @Override
    public int getVl() {
        return vl;
    }

    @Override
    public CheckType getCheckType() {
        return alertGroup.getCheckType();
    }

    @Override
    public AlertType getAlertType() {
        return ALERT_TYPE;
    }
}
