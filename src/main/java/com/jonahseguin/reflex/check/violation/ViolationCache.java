/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.violation;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.RTimer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ViolationCache implements RTimer {

    private final Map<String, CheckViolation> violations = new HashMap<>();

    public ViolationCache(Reflex instance) {
        instance.getReflexTimer().registerTimer(this);
    }

    public CheckViolation getViolation(String id) {
        return violations.get(id.toLowerCase());
    }

    @Override
    public void runTimer() {
        Iterator<String> it = violations.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            CheckViolation v = violations.get(key);
            if (System.currentTimeMillis() >= v.getExpiryTime()) {
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
        violations.put(violation.getId(), renewCache(violation)); // renews the infraction
        violation.getReflexPlayer().getRecord().getViolationIDs().add(violation.getId());
    }

    public void uncacheViolation(CheckViolation violation) {
        violations.remove(violation.getId());
        violation.getReflexPlayer().getRecord().getViolationIDs().remove(violation.getId());
    }

    public long getViolationCacheExpiryTimeMS() {
        return (Reflex.getInstance().getReflexConfig().getViolationCacheExpiryMinutes() * 60 * 1000);
    }

}
