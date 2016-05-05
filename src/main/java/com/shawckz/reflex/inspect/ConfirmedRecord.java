/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.inspect;

import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.database.mongo.AutoMongo;
import com.shawckz.reflex.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.util.serial.CheckDataSerializer;
import com.shawckz.reflex.util.serial.RDatabaseSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@CollectionName(name = "reflectconfirmedrecords")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConfirmedRecord extends AutoMongo {

    @MongoColumn(name = "_id", identifier = true)
    private String id = UUID.randomUUID().toString();

    @MongoColumn(name = "confirmer")
    private String confirmer;//username of player who confirmed this record

    @MongoColumn(name = "data")
    @RDatabaseSerializer(serializer = CheckDataSerializer.class)
    private Checker data;

    @MongoColumn(name = "result")
    private boolean result;

}
