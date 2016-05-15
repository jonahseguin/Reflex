/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.shawckz.reflex.backend.configuration.AbstractSerializer;
import com.shawckz.reflex.check.base.RCheckType;

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
