/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.prevent.check;

import com.shawckz.reflex.bridge.RViolation;
import com.shawckz.reflex.core.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.core.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.reflex.core.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.util.serial.CheckDataSerializer;
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
@CollectionName(name = "reflex_checkviolations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CheckViolation implements RViolation {

    @MongoColumn(identifier = true, name = "_id")
    private String id = UUID.randomUUID().toString();

    @MongoColumn(name = "uniqueId")
    private String uniqueId;//Player's uniqueId

    @MongoColumn(name = "time")
    private long time;

    @MongoColumn(name = "checkData")
    @DatabaseSerializer(serializer = CheckDataSerializer.class)
    private CheckData checkData;

    public CheckViolation(String uniqueId, CheckData checkData) {
        this.id = UUID.randomUUID().toString();
        this.time = System.currentTimeMillis();
        this.uniqueId = uniqueId;
        this.checkData = checkData;
    }




}
