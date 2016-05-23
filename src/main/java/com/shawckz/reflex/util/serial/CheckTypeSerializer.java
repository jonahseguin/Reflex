/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.shawckz.reflex.backend.configuration.AbstractSerializer;
import com.shawckz.reflex.check.base.CheckType;

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
