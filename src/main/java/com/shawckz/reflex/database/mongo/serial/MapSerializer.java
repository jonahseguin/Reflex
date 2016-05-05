package com.shawckz.reflex.database.mongo.serial;

import com.shawckz.reflex.configuration.AbstractSerializer;
import com.mongodb.util.JSON;

import java.util.HashMap;

public class MapSerializer extends AbstractSerializer<HashMap> {

    @Override
    public String toString(HashMap data) {
        return JSON.serialize(data);
    }

    @Override
    public HashMap fromString(Object data) {
        HashMap map = (HashMap) JSON.parse(((String)data));
        return map;
    }
}
