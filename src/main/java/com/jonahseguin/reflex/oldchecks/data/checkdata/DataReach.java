/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data.checkdata;

import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.oldchecks.data.CheckData;
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
