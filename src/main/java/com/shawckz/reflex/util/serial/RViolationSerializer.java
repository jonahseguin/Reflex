/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.mongodb.BasicDBObject;
import com.shawckz.reflex.backend.configuration.AbstractSerializer;
import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.check.base.RViolation;
import com.shawckz.reflex.util.utility.ReflexException;

public class RViolationSerializer extends AbstractSerializer<RViolation> {

    @Override
    public String toString(RViolation data) {
        data.update();
        return data.getId();
    }

    @Override
    public RViolation fromString(Object data) {
        if(data instanceof String) {
            String s = (String) data;
            AutoMongo mongo = RViolation.selectOne(new BasicDBObject("_id", s), RViolation.class);
            if(mongo != null && mongo instanceof RViolation) {
                return (RViolation) mongo;
            }
            else{
                throw new ReflexException("RViolation serializer could not find (or is not same type) violation with ID " + s);
            }
        }
        throw new ReflexException("RViolation serializer fromString data is not String");
    }
}
