/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.base;

import com.shawckz.reflex.Reflex;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

public class ViolationCache {

    private final Map<String, RViolation> violations = new HashMap<>();

    public RViolation getViolation(String id) {
        return violations.get(id.toLowerCase());
    }

    /**
     * Async
     */
    public void saveViolation(final RViolation violation) {
        cacheViolation(violation);
        new BukkitRunnable() {
            @Override
            public void run() {
                violation.update();
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    public void cacheViolation(RViolation violation) {
        violations.put(violation.getId(), violation);
    }

}
