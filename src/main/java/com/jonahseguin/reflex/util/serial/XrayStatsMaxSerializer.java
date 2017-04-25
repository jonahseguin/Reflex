/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.mongodb.util.JSON;
import com.jonahseguin.reflex.oldchecks.data.XrayStats;

import java.util.HashMap;
import java.util.Map;

public class XrayStatsMaxSerializer extends AbstractSerializer<Map<XrayStats.Stat, Double>> {

    @Override
    public String toString(Map<XrayStats.Stat, Double> data) {
        Map<String, Double> s = new HashMap<>();

        for (XrayStats.Stat stat : data.keySet()) {
            s.put(stat.toString(), data.get(stat));
        }

        return JSON.serialize(s);
    }

    @Override
    public Map<XrayStats.Stat, Double> fromString(Object data) {
        HashMap<String, Double> map = (HashMap<String, Double>) JSON.parse(((String) data));

        Map<XrayStats.Stat, Double> s = new HashMap<>();

        for (String k : map.keySet()) {
            s.put(XrayStats.Stat.valueOf(k), map.get(k));
        }

        return s;
    }
}
