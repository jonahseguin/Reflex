/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.checks.combat.CheckFastBow;
import com.jonahseguin.reflex.check.checks.combat.CheckNoSwing;
import com.jonahseguin.reflex.check.checks.movement.*;
import com.jonahseguin.reflex.check.checks.other.*;
import com.jonahseguin.reflex.util.exception.ReflexRuntimeException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 00:30.
 * Project: Reflex
 */
public class CheckManager {

    private final Reflex instance;
    private final ConcurrentMap<CheckType, Check> checks = new ConcurrentHashMap<>();

    public CheckManager(Reflex instance) {
        this.instance = instance;

        registerCheck(new CheckHeadRoll(instance));
        registerCheck(new CheckNoFall(instance));
        registerCheck(new CheckTabComplete(instance));
        registerCheck(new CheckBedFly(instance));
        registerCheck(new CheckPhase(instance));
        registerCheck(new CheckVClip(instance));
        registerCheck(new CheckRegen(instance));
        registerCheck(new CheckAntiKnockback(instance));
        registerCheck(new CheckSpeed(instance));
        registerCheck(new CheckBlockHit(instance));
        registerCheck(new CheckFastBow(instance));
        registerCheck(new CheckFastEat(instance));
        registerCheck(new CheckSelfHit(instance));
        registerCheck(new CheckGodMode(instance));
        registerCheck(new CheckSneak(instance));
        registerCheck(new CheckJesus(instance));
        registerCheck(new CheckMorePackets(instance));
        registerCheck(new CheckNoSwing(instance));
        registerCheck(new CheckFly(instance));
        registerCheck(new CheckStep(instance));

        checks.values().forEach(check -> check.setEnabled(check.isEnabled())); // Register check listeners if enabled
        checks.values().forEach(check -> { // Register check timers if applicable
            if (check instanceof RTimer) {
                instance.getReflexTimer().registerTimer(((RTimer)check));
            }
        });
    }

    public void save() {
        checks.values().forEach(Check::save);
    }

    public void registerCheck(Check check) {
        checks.put(check.getCheckType(), check);
    }

    public void unregisterCheck(Check check) {
        checks.remove(check.getCheckType());
    }

    public Set<Check> getChecks() {
        return new HashSet<>(checks.values());
    }

    public Check getCheck(CheckType checkType) {
        if (!checks.containsKey(checkType)) {
            throw new ReflexRuntimeException("CheckType for " + checkType.getName() + " is not registered");
        }
        return checks.get(checkType);
    }


}
