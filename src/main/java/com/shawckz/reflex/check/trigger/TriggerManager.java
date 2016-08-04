/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger;

import com.google.common.collect.Maps;
import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.simple.*;
import com.shawckz.reflex.check.trigger.triggers.*;
import com.shawckz.reflex.util.utility.ReflexException;

import java.util.concurrent.ConcurrentMap;

public class TriggerManager {

    private final ConcurrentMap<CheckType, RTrigger> triggers = Maps.newConcurrentMap();
    private final Reflex instance;

    public TriggerManager(Reflex instance) {
        this.instance = instance;
    }

    public void setup() {
        //Triggers
        register(new TriggerAutoClick(instance));
        register(new TriggerFastBow(instance));
        register(new TriggerVClip(instance));
        register(new TriggerRegen(instance));
        register(new TriggerReach(instance));
        register(new TriggerFly(instance));

        //Simple checks
        register(new CheckSpeed(instance));
        register(new CheckHeadRoll(instance));
        register(new CheckTabComplete(instance));
        register(new CheckXray(instance));
        register(new CheckMorePackets(instance));
        register(new CheckAccuracy(instance));
        register(new CheckPhase(instance));

        triggers.values().forEach(RTrigger::setupConfig);
    }

    public ConcurrentMap<CheckType, RTrigger> getTriggers() {
        return triggers;
    }

    public RTrigger getTrigger(CheckType checkType) {
        if (triggers.containsKey(checkType)) {
            return triggers.get(checkType);
        }
        throw new ReflexException("No trigger registered for CheckType " + checkType.getName());
    }

    public void register(RTrigger trigger) {
        triggers.put(trigger.getCheckType(), trigger);
        trigger.setEnabled(trigger.isEnabled());
        if (trigger instanceof RTimer) {
            RTimer timer = (RTimer) trigger;
            instance.getReflexTimer().registerTimer(timer);
        }
    }

}
