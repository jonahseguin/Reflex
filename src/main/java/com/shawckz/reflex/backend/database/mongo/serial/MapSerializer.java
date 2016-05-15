/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.backend.database.mongo.serial;

import com.mongodb.util.JSON;
import com.shawckz.reflex.backend.configuration.AbstractSerializer;

import java.util.HashMap;

public class MapSerializer extends AbstractSerializer<HashMap> {

    @Override
    public String toString(HashMap data) {
        return JSON.serialize(data);
    }

    @Override
    public HashMap fromString(Object data) {
        HashMap map = (HashMap) JSON.parse(((String) data));
        return map;
    }
}
