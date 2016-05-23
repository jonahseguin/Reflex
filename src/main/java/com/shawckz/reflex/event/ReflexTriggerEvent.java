/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event;

import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.player.reflex.ReflexPlayer;

public class ReflexTriggerEvent extends ReflexEvent {

    private final RTrigger trigger;
    private final ReflexPlayer player;
    private final CheckType checkType;

    public ReflexTriggerEvent(RTrigger trigger, ReflexPlayer player, CheckType checkType) {
        this.trigger = trigger;
        this.player = player;
        this.checkType = checkType;
    }

    public RTrigger getTrigger() {
        return trigger;
    }

    public ReflexPlayer getPlayer() {
        return player;
    }

    public CheckType getCheckType() {
        return checkType;
    }
}
