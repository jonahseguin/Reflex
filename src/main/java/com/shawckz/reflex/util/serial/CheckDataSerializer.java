/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.mongodb.BasicDBObject;
import com.shawckz.reflex.core.configuration.AbstractSerializer;
import com.shawckz.reflex.core.database.mongo.AutoMongo;
import com.shawckz.reflex.prevent.check.CheckData;
import com.shawckz.reflex.util.ReflexException;

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
        return data.getId();
    }

    @Override
    public CheckData fromString(Object data) {
        if(data instanceof String) {
            String s = (String) data;
            List<AutoMongo> mongos = CheckData.select(new BasicDBObject("_id", s), CheckData.class);
            for(AutoMongo mongo : mongos) {
                if(mongo instanceof CheckData) {
                    return (CheckData) mongo;
                }
            }

        }
        throw new ReflexException("Cannot serialize - data is not string");
    }
}
