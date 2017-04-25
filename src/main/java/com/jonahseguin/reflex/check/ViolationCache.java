/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import java.util.HashMap;
import java.util.Map;

public class ViolationCache {

    private final Map<String, CheckViolation> violations = new HashMap<>();

    public CheckViolation getViolation(String id) {
        return violations.get(id.toLowerCase());
    }

    public void cacheViolation(CheckViolation violation) {
        violations.put(violation.getId(), violation);
    }


}
