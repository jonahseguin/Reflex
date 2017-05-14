/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.violation;

import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.jonahseguin.reflex.backend.database.mongo.annotations.MongoColumn;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.serial.CheckTypeSerializer;
import com.jonahseguin.reflex.util.serial.ReflexPlayerSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by Jonah Seguin on Sat 2017-05-13 at 10:14.
 * Project: Reflex
 */
@Getter
@Setter
public class Infraction extends AutoMongo {

    /*
    Permanent Violation

    Will be stored "permanently" on a player's record

    "Permanent record" --> in contrast to normal violations that are locally cached and expire after some time

    These should only be saved if a normal infraction happens X times within X timeframe, on the same check

    These will be saved on a player's record

    Should store much more data than a normal infraction

     */

    @MongoColumn(name = "id", identifier = true)
    private String id;

    @MongoColumn(name = "player")
    @DatabaseSerializer(serializer = ReflexPlayerSerializer.class)
    private ReflexPlayer reflexPlayer;

    @MongoColumn(name = "check")
    @DatabaseSerializer(serializer = CheckTypeSerializer.class)
    private CheckType checkType;

    @MongoColumn(name = "time")
    private long time;

    @MongoColumn(name = "detail")
    private String detail;

    @MongoColumn(name = "violationCount")
    private int violationCount;

    public Infraction(ReflexPlayer reflexPlayer, CheckType checkType, int violationCount, String detail) {
        this.id = UUID.randomUUID().toString();
        this.reflexPlayer = reflexPlayer;
        this.checkType = checkType;
        this.time = System.currentTimeMillis();
        this.violationCount = violationCount;
        this.detail = detail;
    }

    public Infraction() {
        // AutoMongo
    }
}
