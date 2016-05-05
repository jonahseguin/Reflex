/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.mongodb.BasicDBObject;
import com.shawckz.reflex.autoban.BanRecord;
import com.shawckz.reflex.configuration.AbstractSerializer;
import com.shawckz.reflex.database.mongo.AutoMongo;
import com.shawckz.reflex.util.ReflexException;

import java.util.List;

public class BanRecordSerializer extends AbstractSerializer<BanRecord> {

    @Override
    public String toString(BanRecord object) {
        return object.getId();
    }

    @Override
    public BanRecord fromString(Object object) {
        if(object instanceof String) {
            String s = (String) object;
            List<AutoMongo> ret =  BanRecord.select(new BasicDBObject("_id", s), BanRecord.class);
            for(AutoMongo r : ret) {
                if(r instanceof BanRecord) {
                    return (BanRecord) r;
                }
            }
        }
        throw new ReflexException("Object is not string for deserialization");
    }
}
