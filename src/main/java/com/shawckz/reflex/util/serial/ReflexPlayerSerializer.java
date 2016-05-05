package com.shawckz.reflex.util.serial;

import com.shawckz.reflex.configuration.AbstractSerializer;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;
import com.shawckz.reflex.util.ReflexException;

public class ReflexPlayerSerializer extends AbstractSerializer<ReflexPlayer> {

    @Override
    public String toString(ReflexPlayer data) {
        return data.getUniqueId();
    }

    @Override
    public ReflexPlayer fromString(Object data) {
        if(data instanceof String) {
            String s = (String) data;
            return ReflexCache.get().getAresPlayerByUUID(s);
        }
        throw new ReflexException("Could not deserialize ReflexPlayer (data is not String)");
    }
}
