/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.util.utility.ReflexException;
import org.bson.Document;

public class RViolationSerializer extends AbstractSerializer<RViolation> {

    @Override
    public String toString(RViolation data) {
        return data.getId();
    }

    @Override
    public RViolation fromString(Object data) {
        if (data instanceof String) {
            String s = (String) data;
            AutoMongo mongo = RViolation.selectOne(new Document("_id", s), RViolation.class);
            if (mongo != null && mongo instanceof RViolation) {
                return (RViolation) mongo;
            } else {
                throw new ReflexException("RViolation serializer could not find (or is not same type) infraction with ID " + s);
            }
        }
        throw new ReflexException("RViolation serializer fromString data is not String");
    }
}
