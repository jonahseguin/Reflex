/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.ban;

import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AutobanManager {

    private final ConcurrentMap<String, Autoban> autobans = new ConcurrentHashMap<>();

    public Autoban getAutoban(String name) {
        return autobans.get(name.toLowerCase());
    }

    public ConcurrentMap<String, Autoban> getAutobans() {
        return autobans;
    }

    public boolean hasAutoban(String name) {
        return autobans.containsKey(name.toLowerCase()) && !autobans.get(name.toLowerCase()).isCancelled();
    }

    public boolean hasAutoban(ReflexPlayer reflexPlayer) {
        return hasAutoban(reflexPlayer.getName());
    }

    public void putAutoban(Autoban autoban) {
        autobans.put(autoban.getPlayer().getName().toLowerCase(), autoban);
    }

    public void removeAutoban(String name) {
        if (autobans.containsKey(name.toLowerCase())) {
            if (!autobans.get(name.toLowerCase()).isCancelled()) {
                autobans.get(name.toLowerCase()).setCancelled(true);
            }
            autobans.remove(name.toLowerCase());
        }
    }

}
