package com.shawckz.reflex.autoban;

import java.util.HashMap;
import java.util.Map;

public class AutobanManager {

    private final static Map<String, Autoban> autobans = new HashMap<>();

    public synchronized static Autoban getAutoban(String name){
        return autobans.get(name);
    }

    public synchronized static boolean hasAutoban(String name) {
        return autobans.containsKey(name) && !autobans.get(name).isCancelled();
    }

    public synchronized static void putAutoban(Autoban autoban){
        //if(!hasAutoban(autoban.getName())){
        autobans.put(autoban.getName(),autoban);
        //}
    }
}
