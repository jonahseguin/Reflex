/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.simple.*;
import com.shawckz.reflex.check.trigger.triggers.*;
import com.shawckz.reflex.util.utility.ReflexException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TriggerManager {

    private ConcurrentMap<CheckType, RTrigger> triggers = new ConcurrentHashMap<>();

    public TriggerManager(Reflex instance) {

    }

    public void setup() {
        //Triggers
        register(new TriggerAutoClick());
        register(new TriggerFastBow());
        register(new TriggerVClip());
        register(new TriggerRegen());
        register(new TriggerReach());
        register(new TriggerFly());
        register(new TriggerPhase());
    //    register(new TriggerCriticals());

        //Simple checks
        register(new CheckSpeed());
        register(new CheckHeadRoll());
        register(new CheckTabComplete());
        register(new CheckXray());
        register(new CheckNoSwing());
        register(new CheckMorePackets());
        register(new CheckAuraTwitch());
        register(new CheckAccuracy());

        triggers.values().stream().forEach(RTrigger::setupConfig);
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
            Reflex.getInstance().getReflexTimer().registerTimer(timer);
        }
    }

}
