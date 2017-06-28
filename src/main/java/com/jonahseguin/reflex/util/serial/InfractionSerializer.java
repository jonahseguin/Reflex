/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.check.violation.Infraction;
import com.jonahseguin.reflex.util.exception.AbstractSerializerException;
import org.bson.Document;

/**
 * Created by Jonah Seguin on Thu 2017-05-25 at 20:03.
 * Project: Reflex
 */
public class InfractionSerializer extends AbstractSerializer<Infraction> {

    @Override
    public String toString(Infraction data) {
        data.update();
        return data.getId();
    }

    @Override
    public Infraction fromString(Object data) throws AbstractSerializerException {
        if (data instanceof String) {
            String s = (String) data;
            AutoMongo mongo = Infraction.selectOne(new Document("id", s), Infraction.class);
            if (mongo != null) {
                if (mongo instanceof Infraction) {
                    return (Infraction) mongo;
                } else {
                    throw new AbstractSerializerException("Could not deserialize Infraction; mongo not Infraction");
                }
            } else {
                return null;
            }
        } else {
            throw new AbstractSerializerException("Could not deserialize Infraction: data not string");
        }
    }
}
