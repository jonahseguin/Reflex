/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultData;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultType;
import com.jonahseguin.reflex.util.utility.ReflexException;
import org.bson.Document;

public class RInspectResultDataSerializer extends AbstractSerializer<RInspectResultData> {

    public static final String KEY_TYPE = "type";
    public static final String KEY_DETAIL = "detail";

    @Override
    public String toString(RInspectResultData data) {
        Document document = new Document(KEY_TYPE, data.getType().toString());
        document.append(KEY_DETAIL, (data.getDetail() == null ? "" : data.getDetail()));
        return document.toJson();
    }

    @Override
    public RInspectResultData fromString(Object data) {
        if (data instanceof String) {
            String s = (String) data;
            Document document = Document.parse(s);
            RInspectResultType type = RInspectResultType.valueOf(document.getString(KEY_TYPE).toUpperCase());
            String detail = document.getString(KEY_DETAIL);
            if (detail.equals("")) {
                detail = null;
            }

            return new RInspectResultData(type, detail);
        }
        throw new ReflexException("RInspectResultData serializer data is not String");
    }
}
