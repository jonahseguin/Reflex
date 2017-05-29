/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.obj;

import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.jonahseguin.reflex.backend.database.mongo.annotations.MongoColumn;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.serial.ReflexPlayerSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by Jonah Seguin on Sun 2017-05-28 at 12:33.
 * Project: Reflex
 */
@CollectionName(name = "reflex_notes")
@Getter
@Setter
public class Note extends AutoMongo {

    @MongoColumn(name = "_id", identifier = true)
    private String id;

    @MongoColumn(name = "player")
    @DatabaseSerializer(serializer = ReflexPlayerSerializer.class)
    private ReflexPlayer player;

    @MongoColumn(name = "author")
    @DatabaseSerializer(serializer = ReflexPlayerSerializer.class)
    private ReflexPlayer author;

    @MongoColumn(name = "note")
    private String note;

    public Note() {
        // For AutoMongo
    }

    public Note(ReflexPlayer player, ReflexPlayer author, String note) {
        this.id = UUID.randomUUID().toString();
        this.player = player;
        this.author = author;
        this.note = note;
    }
}
