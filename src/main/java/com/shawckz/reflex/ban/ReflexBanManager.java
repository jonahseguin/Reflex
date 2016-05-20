/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.ban;

import com.mongodb.BasicDBObject;
import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.database.mongo.AutoMongo;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

public class ReflexBanManager {

    private Map<String, ReflexBan> cache = new HashMap<>();

    public boolean hasBan(String uniqueId) {
        uniqueId = uniqueId.toLowerCase();
        if(cache.containsKey(uniqueId)) return true;

        ReflexBan ban = getBan(uniqueId);

        return ban != null;
    }

    public ReflexBan getBan(String uniqueId) {
        uniqueId = uniqueId.toLowerCase();
        if(cache.containsKey(uniqueId)) {
            return cache.get(uniqueId);
        }
        else{
            AutoMongo mongo = ReflexBan.selectOne(new BasicDBObject("uniqueId", uniqueId), ReflexBan.class);
            if(mongo != null && mongo instanceof ReflexBan) {
                ReflexBan ban = (ReflexBan) mongo;
                cacheBan(ban);
                return ban;
            }
        }
        return null;
    }

    public void cacheBan(ReflexBan ban) {
        cache.put(ban.getUniqueId().toLowerCase(), ban);
    }

    public void saveBan(final ReflexBan ban) {
        cacheBan(ban);
        new BukkitRunnable(){
            @Override
            public void run() {
                ban.update();
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

}
