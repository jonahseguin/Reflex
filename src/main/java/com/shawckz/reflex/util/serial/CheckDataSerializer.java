/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.mongodb.BasicDBObject;
import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.database.mongo.AutoMongo;
import com.shawckz.reflex.util.ReflexException;

import java.util.List;

public class CheckDataSerializer extends ReflexSerializer<Checker> {

    @Override
    public String toString(Checker object) {
        return object.getId();
    }

    @Override
    public Checker fromString(Object object, Class<? extends Checker> type) {
        if(object instanceof String) {
            String s = (String) object;
            List<AutoMongo> ret =  Checker.select(new BasicDBObject("_id", s), type);
            for(AutoMongo r : ret) {
                if(r instanceof Checker) {
                    return (Checker) r;
                }
            }
        }
        throw new ReflexException("Object is not string for deserialization");
    }
}
