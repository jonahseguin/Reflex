/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.api;

import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.data.CheckData;
import com.jonahseguin.reflex.oldchecks.inspect.InspectManager;
import com.jonahseguin.reflex.oldchecks.inspect.RInspect;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

/**
 * Called when any data is inspected by {@link InspectManager#inspect}
 */
public class ReflexInspectEvent extends ReflexAPIEvent {

    private final RInspect inspector;
    private final ReflexPlayer player;
    private final CheckData data;
    private final CheckType checkType;
    private final int dataPeriod;

    public ReflexInspectEvent(RInspect inspector, ReflexPlayer player, CheckData data, CheckType checkType, int dataPeriod) {
        this.inspector = inspector;
        this.player = player;
        this.data = data;
        this.checkType = checkType;
        this.dataPeriod = dataPeriod;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public RInspect getInspector() {
        return inspector;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }

    public CheckData getData() {
        return data;
    }

    public int getDataPeriod() {
        return dataPeriod;
    }
}
