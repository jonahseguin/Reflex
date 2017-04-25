/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data.checkdata;

import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.oldchecks.data.CheckData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CollectionName(name = "reflex_checkdata")
public class DataFly extends CheckData {

    private double yps = 0;//y per second
    private double peakYps = 0;
    private double peakBps = 0;
    private double bps = 0;//blocks per second
    private boolean hasPositiveVelocity = false;//moving upwards
    private long lastGroundTime = -1;//last system#currenttimemillis that they touched the ground
    private int peakAirTime = 0;

}
