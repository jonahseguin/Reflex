/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.inspect;

import com.shawckz.reflex.check.checker.CheckerSimilarity;
import lombok.Data;

@Data
public class ReflexInspectionResult {

    enum Action {
        AUTOBAN,
        STAFF_APPROVAL,
        LOG,
        NONE
    }

    enum Result {
        COULD_NOT_DECIDE, //Same positive as negative
        NO_RECORDS_FOUND,
        POSITIVE,
        NEGATIVE
    }

    private final long time = System.currentTimeMillis();
    private final Action[] actions;//Cannot be null
    private final CheckerSimilarity similarity;//Can be null
    private final ConfirmedRecord record;
    private final Result result;//Cannot be null

}
