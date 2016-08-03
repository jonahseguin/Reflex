/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.serial;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.AbstractSerializer;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.utility.ReflexException;

public class ReflexPlayerSerializer extends AbstractSerializer<ReflexPlayer> {

    @Override
    public String toString(ReflexPlayer data) {
        return data.getUniqueId();
    }

    @Override
    public ReflexPlayer fromString(Object data) {
        if (data instanceof String) {
            String s = (String) data;
            return Reflex.getInstance().getCache().getReflexPlayerByUniqueId(s);
        }
        throw new ReflexException("Could not deserialize ReflexPlayer (data is not String)");
    }
}
