/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.checkdata;

import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.check.data.CheckData;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@CollectionName(name = "reflex_checkdata")
public class DataReach extends CheckData {

    //Distance, ping
    private Set<Map.Entry<Double, Integer>> entries = new HashSet<>();

    private double peakReach = 0;


}
