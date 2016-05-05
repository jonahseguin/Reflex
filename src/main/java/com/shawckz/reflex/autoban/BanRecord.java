package com.shawckz.reflex.autoban;

import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.database.mongo.AutoMongo;
import com.shawckz.reflex.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.util.serial.CheckDataSerializer;
import com.shawckz.reflex.util.serial.RDatabaseSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CollectionName(name = "reflexbans")
public class BanRecord extends AutoMongo {

    @MongoColumn(name = "_id", identifier = true)
    private String id;//ban record id

    @MongoColumn(name = "check")
    private CheckType check;

    @MongoColumn(name = "time")
    private long time;

    @MongoColumn(name = "uniqueId")
    private String uniqueId;//banned player's UUID

    @MongoColumn(name = "data")
    @RDatabaseSerializer(serializer = CheckDataSerializer.class)
    private Checker data;


}
