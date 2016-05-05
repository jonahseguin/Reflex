package com.shawckz.reflex.check;

import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.database.mongo.AutoMongo;
import com.shawckz.reflex.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.reflex.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.player.ReflexPlayer;
import com.shawckz.reflex.util.serial.CheckDataSerializer;
import com.shawckz.reflex.util.serial.RDatabaseSerializer;
import com.shawckz.reflex.util.serial.ReflexPlayerSerializer;
import lombok.Data;

import java.util.UUID;

@Data
@CollectionName(name = "aresviolations")
public class Violation extends AutoMongo{

    @MongoColumn(name = "player")
    @DatabaseSerializer(serializer = ReflexPlayerSerializer.class)
    private final ReflexPlayer player;

    @MongoColumn(name = "totalVL")
    private final int totalVL;

    @MongoColumn(name = "check")
    private final CheckType check;

    @MongoColumn(name = "vl")
    private final int vl;

    @MongoColumn(name = "id", identifier = true)
    private final String id = UUID.randomUUID().toString().substring(0,6);

    @MongoColumn(name = "cancelled")
    private final boolean cancelled;

    @MongoColumn(name = "checkdata")
    @RDatabaseSerializer(serializer = CheckDataSerializer.class)
    private final Checker data;

}
