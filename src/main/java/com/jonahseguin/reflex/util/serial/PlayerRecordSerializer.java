/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.check.violation.PlayerRecord;
import com.jonahseguin.reflex.util.exception.AbstractSerializerException;
import org.bson.Document;

/**
 * Created by Jonah Seguin on Sun 2017-05-14 at 16:22.
 * Project: Reflex
 */
public class PlayerRecordSerializer extends AbstractSerializer<PlayerRecord> {

    @Override
    public String toString(PlayerRecord data) {
        return data.toDocument().toJson();
    }

    @Override
    public PlayerRecord fromString(Object data) throws AbstractSerializerException {
        if (data instanceof String) {
            String s = (String) data;
            Document document = Document.parse(s);
            AutoMongo mongo = PlayerRecord.fromDocument(PlayerRecord.class, document);
            if (mongo instanceof PlayerRecord) {
                return ((PlayerRecord) mongo);
            } else {
                throw new AbstractSerializerException("Could not deserialize PlayerRecord: mongo not PlayerRecord");
            }
        } else {
            throw new AbstractSerializerException("Could not deserialize PlayerRecord: data not string");
        }
    }
}
