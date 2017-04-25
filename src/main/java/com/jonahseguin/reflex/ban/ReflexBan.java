/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.ban;

import com.jonahseguin.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.jonahseguin.reflex.backend.database.mongo.annotations.MongoColumn;
import com.jonahseguin.reflex.oldchecks.base.RViolation;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.util.serial.RViolationSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * A custom "Reflex Ban" - Created and used when the plugin is the the REFLEX ban mode via the config.
 * However, existing ReflexBans will still be enforced if the plugin is not in REFLEX ban mode.
 */
@CollectionName(name = "reflex_bans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReflexBan extends AutoMongo {

    @MongoColumn(name = "_id", identifier = true)
    private String id;

    @MongoColumn(name = "uniqueId")
    private String uniqueId;

    @MongoColumn(name = "violation")
    @DatabaseSerializer(serializer = RViolationSerializer.class)
    private RViolation violation;

    @MongoColumn(name = "banned")
    private boolean banned = true;

    @MongoColumn(name = "time")
    private long time;

    @MongoColumn(name = "confirmed")
    private boolean confirmed = false;

    @MongoColumn(name = "bannecCorrectly")
    private boolean bannedCorrectly = true;

    @MongoColumn(name = "expiration")
    private long expiration;

    public ReflexBan(String uniqueId, RViolation violation, long expiration) {
        this.id = UUID.randomUUID().toString();
        this.uniqueId = uniqueId;
        this.violation = violation;
        this.time = System.currentTimeMillis();
        this.expiration = expiration;
    }

    public boolean isActive() {
        return isBanned() && ((isConfirmed() && isBannedCorrectly()) || (getExpiration() > System.currentTimeMillis()));
    }

}
