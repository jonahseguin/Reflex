/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.inspect;

import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.reflex.backend.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.check.base.RViolation;
import com.shawckz.reflex.util.serial.RInspectResultDataSerializer;
import com.shawckz.reflex.util.serial.RViolationSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CollectionName(name = "reflex_inspections")
public class RInspectResult extends AutoMongo {

    @MongoColumn(name = "_id", identifier = true)
    private String id;

    @MongoColumn(name = "data")
    @DatabaseSerializer(serializer = RInspectResultDataSerializer.class)
    private RInspectResultData data;

    @MongoColumn(name = "violation")
    @DatabaseSerializer(serializer = RViolationSerializer.class)
    private RViolation violation;

    @MongoColumn(name = "period")
    private int inspectionPeriod;

    public RInspectResult(RInspectResultData data, RViolation violation, int inspectionPeriod) {
        this.id = UUID.randomUUID().toString();
        this.data = data;
        this.violation = violation;
        this.inspectionPeriod = inspectionPeriod;
    }


}