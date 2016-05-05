/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.inspect;

import java.util.HashSet;
import java.util.Set;

public class RecordCache {

    private static final Set<ConfirmedRecord> records = new HashSet<>();

    //TODO: Load in onEnable

    public static void cache(ConfirmedRecord checker) {
        records.add(checker);
    }

    public static void uncache(ConfirmedRecord checker) {
        if(records.contains(checker)) {
            records.remove(checker);
        }
    }

    public static Set<ConfirmedRecord> getData() {
        return records;
    }
}
