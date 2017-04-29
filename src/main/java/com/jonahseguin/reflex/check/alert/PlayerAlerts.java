/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:54.
 * Project: Reflex
 */
public class PlayerAlerts {

    private final ReflexPlayer player;
    private final Map<CheckType, AlertGroup> alertGroups = new HashMap<>();

    public PlayerAlerts(ReflexPlayer player) {
        this.player = player;
    }

    public AlertGroup getAlertGroup(CheckType checkType) {
        if (alertGroups.containsKey(checkType)) {
            return alertGroups.get(checkType);
        } else {
            return alertGroups.put(checkType, new AlertGroup(player, checkType));
        }
    }

    public boolean shouldGroupAlert(CheckType checkType) {
        return alertGroups.containsKey(checkType) && !alertGroups.get(checkType).getAlerts().isEmpty();
    }

}
