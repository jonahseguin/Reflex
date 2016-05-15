/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.shawckz.reflex.backend.configuration.AbstractSerializer;
import com.shawckz.reflex.util.obj.AutobanMethod;

public class AutobanMethodSerializer extends AbstractSerializer<AutobanMethod> {

    @Override
    public String toString(AutobanMethod data) {
        return data.toString();
    }

    @Override
    public AutobanMethod fromString(Object data) {
        return AutobanMethod.valueOf(((String) data).toUpperCase());
    }
}
