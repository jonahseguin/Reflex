/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.alert;

import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:54.
 * Project: Reflex
 */
public class PlayerAlerts {

    private final ReflexPlayer player;
    private final ConcurrentMap<CheckType, AlertSet> alertGroups = new ConcurrentHashMap<>();

    public PlayerAlerts(ReflexPlayer player) {
        this.player = player;
    }

    public AlertSet getAlertGroup(CheckType checkType) {
        if (alertGroups.containsKey(checkType)) {
            return alertGroups.get(checkType);
        } else {
            alertGroups.put(checkType, new AlertSet(player, checkType));
            return alertGroups.get(checkType);
        }
    }

    public void removeAlertGroup(CheckType checkType) {
        alertGroups.remove(checkType);
    }

    public ReflexPlayer getPlayer() {
        return player;
    }

    public Map<CheckType, AlertSet> getAlertGroups() {
        return alertGroups;
    }

    public boolean isEmpty() {
        return alertGroups.isEmpty();
    }

}
