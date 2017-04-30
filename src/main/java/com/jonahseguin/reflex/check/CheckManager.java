/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.checks.movement.CheckBedFly;
import com.jonahseguin.reflex.check.checks.movement.CheckHeadRoll;
import com.jonahseguin.reflex.check.checks.movement.CheckNoFall;
import com.jonahseguin.reflex.check.checks.other.CheckTabComplete;
import com.jonahseguin.reflex.util.utility.ReflexException;

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


        checks.values().forEach(check -> check.setEnabled(check.isEnabled())); // Register check listeners if enabled
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

    public Check getCheck(CheckType checkType) {
        if (!checks.containsKey(checkType)) {
            throw new ReflexException("CheckType for " + checkType.getName() + " is not registered");
        }
        return checks.get(checkType);
    }


}
