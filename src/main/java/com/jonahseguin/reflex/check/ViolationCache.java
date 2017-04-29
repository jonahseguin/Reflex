/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.oldchecks.base.RTimer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ViolationCache implements RTimer {

    private final Map<String, CheckViolation> violations = new HashMap<>();

    public CheckViolation getViolation(String id) {
        return violations.get(id.toLowerCase());
    }

    public ViolationCache(Reflex instance) {
        instance.getReflexTimer().registerTimer(this);
    }

    @Override
    public void runTimer() {
        Iterator<String> it = violations.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            CheckViolation v = violations.get(key);
            if(System.currentTimeMillis() >= v.getExpiryTime()) {
                //Expired
                uncacheViolation(v);
            }
        }
    }

    public CheckViolation renewCache(CheckViolation violation) {
        violation.setExpiryTime(System.currentTimeMillis() + (Reflex.getInstance().getReflexConfig().getViolationCacheExpiryMinutes() * 60 * 1000));
        return violation;
    }

    public void cacheViolation(CheckViolation violation) {
        violations.put(violation.getId(), renewCache(violation)); // renews the violation
    }

    public void uncacheViolation(CheckViolation violation) {
        violations.remove(violation.getId());
    }

}
