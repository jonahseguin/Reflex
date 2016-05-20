/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.ban;

import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.reflex.backend.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.check.base.RViolation;
import com.shawckz.reflex.util.serial.RViolationSerializer;
import lombok.Getter;
import lombok.Setter;

@CollectionName(name = "reflex_bans")
@Getter
@Setter
public class ReflexBan extends AutoMongo {

    @MongoColumn(name = "_id", identifier = true)
    private String id;

    @MongoColumn(name = "uniqueId")
    private String uniqueId;

    @MongoColumn(name = "violation")
    @DatabaseSerializer(serializer = RViolationSerializer.class)
    private RViolation violation;

    @MongoColumn(name = "time")
    private long time;

    @MongoColumn(name = "confirmed")
    private boolean confirmed = false;

    @MongoColumn(name = "bannecCorrectly")
    private boolean bannedCorrectly = true;

}
