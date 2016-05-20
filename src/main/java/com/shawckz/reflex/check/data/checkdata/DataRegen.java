/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.checkdata;

import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.check.data.CheckData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CollectionName(name = "reflex_checkdata")
public class DataRegen extends CheckData {

    private double healthRegenerated = 0;

    private double hps = 0;


}
