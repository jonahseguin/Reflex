/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;

public class RCheckTypeSerializer extends AbstractSerializer<RCheckType> {

    @Override
    public String toString(RCheckType data) {
        return data.toString();
    }

    @Override
    public RCheckType fromString(Object data) {
        return RCheckType.valueOf(((String) data).toUpperCase());
    }
}