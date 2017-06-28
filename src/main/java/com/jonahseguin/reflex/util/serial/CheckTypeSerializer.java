/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.check.CheckType;

public class CheckTypeSerializer extends AbstractSerializer<CheckType> {

    @Override
    public String toString(CheckType data) {
        return data.toString();
    }

    @Override
    public CheckType fromString(Object data) {
        return CheckType.fromString(((String) data));
    }
}
