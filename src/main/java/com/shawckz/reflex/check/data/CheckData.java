/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data;

import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.backend.database.mongo.annotations.MongoColumn;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@CollectionName(name = "reflex_checkdata")
public class CheckData extends AutoMongo {

    @MongoColumn(name = "_id", identifier = true)
    private String id = UUID.randomUUID().toString();

    @MongoColumn(name = "ping")
    private int ping = 0;

    @MongoColumn(name = "tps")
    private double tps = 20;

    public CheckData() {
        //No args constructor
    }


}
