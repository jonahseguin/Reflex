/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.base;

import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.reflex.backend.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.check.data.CheckData;
import com.shawckz.reflex.util.serial.CheckDataSerializer;
import com.shawckz.reflex.util.serial.CheckTypeSerializer;
import com.shawckz.reflex.util.serial.RCheckTypeSerializer;
import com.shawckz.reflex.util.serial.RDatabaseSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@CollectionName(name = "reflex_violations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RViolation extends AutoMongo {

    @MongoColumn(identifier = true, name = "_id")
    private String id = UUID.randomUUID().toString();

    @MongoColumn(name = "uniqueId")
    private String uniqueId;//Player's uniqueId

    @MongoColumn(name = "time")
    private long time;

    @MongoColumn(name = "check")
    @DatabaseSerializer(serializer = CheckTypeSerializer.class)
    private CheckType checkType;

    @MongoColumn(name = "checkData")
    @RDatabaseSerializer(serializer = CheckDataSerializer.class)
    private CheckData data;

    @MongoColumn(name = "source")
    @DatabaseSerializer(serializer = RCheckTypeSerializer.class)
    private RCheckType source;

    @MongoColumn(name = "vl")
    private int vl;

    public RViolation(String uniqueId, CheckData data, CheckType checkType, RCheckType source, int vl) {
        this.id = UUID.randomUUID().toString();
        this.time = System.currentTimeMillis();
        this.uniqueId = uniqueId;
        this.data = data;
        this.source = source;
        this.checkType = checkType;
        this.vl = vl;
    }


}
