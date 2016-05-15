package com.shawckz.reflex.autoban;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AutobanManager {

    private final ConcurrentMap<String, Autoban> autobans = new ConcurrentHashMap<>();

    public Autoban getAutoban(String name) {
        return autobans.get(name.toLowerCase());
    }

    public boolean hasAutoban(String name) {
        return autobans.containsKey(name.toLowerCase()) && !autobans.get(name.toLowerCase()).isCancelled();
    }

    public void putAutoban(Autoban autoban) {
        autobans.put(autoban.getPlayer().getName().toLowerCase(), autoban);
    }

    public void removeAutoban(String name) {
        if(autobans.containsKey(name.toLowerCase())) {
            autobans.remove(name.toLowerCase());
        }
    }

}
