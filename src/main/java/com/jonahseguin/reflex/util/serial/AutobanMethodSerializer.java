/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.util.obj.AutobanMethod;

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
