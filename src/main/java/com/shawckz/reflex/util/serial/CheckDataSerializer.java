/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.mongodb.BasicDBObject;
import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.check.data.CheckData;

import java.util.List;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class CheckDataSerializer extends ReflexSerializer<CheckData> {

    @Override
    public String toString(CheckData data) {
        if (data != null) {
            return data.getId();
        }
        return null;
    }

    @Override
    public CheckData fromString(Object data, Class<? extends CheckData> type) {
        String s = (String) data;
        List<AutoMongo> mongos = AutoMongo.select(new BasicDBObject("_id", s), type);
        for (AutoMongo mongo : mongos) {
            if (mongo instanceof CheckData) {
                return (CheckData) mongo;
            }
        }
        return null;
    }
}
