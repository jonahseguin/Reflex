/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.mongodb.BasicDBObject;
import com.shawckz.reflex.backend.configuration.AbstractSerializer;
import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.check.data.CheckData;
import com.shawckz.reflex.check.data.PlayerData;
import com.shawckz.reflex.util.utility.ReflexException;

import java.util.List;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class CheckDataSerializer extends AbstractSerializer<CheckData> {

    @Override
    public String toString(CheckData data) {
        if (data != null) {
            return data.getId();
        }
        return null;
    }

    @Override
    public CheckData fromString(Object data) {
        if (data instanceof String) {
            String s = (String) data;
            List<AutoMongo> mongos = PlayerData.select(new BasicDBObject("_id", s), CheckData.class);
            for (AutoMongo mongo : mongos) {
                if (mongo instanceof CheckData) {
                    return (CheckData) mongo;
                }
            }

        }
        throw new ReflexException("Cannot deserialize - data is not string");
    }
}
