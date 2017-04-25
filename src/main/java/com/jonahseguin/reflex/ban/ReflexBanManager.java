/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.ban;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import org.bson.Document;

import java.util.*;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handles the Reflex BanCache and provides utility methods for ReflexBans
 */
public class ReflexBanManager {

    private Map<String, BanCache> cache = new HashMap<>();

    public boolean hasBan(String uniqueId) {
        if (cache.containsKey(uniqueId)) {
            if (cache.get(uniqueId).hasActiveBan()) {
                return true;
            }
        }

        ReflexBan ban = getBan(uniqueId);

        return ban != null;
    }

    public ReflexBan getBan(String uniqueId) {
        if (cache.containsKey(uniqueId)) {
            if (cache.get(uniqueId).hasActiveBan()) {
                return cache.get(uniqueId).getActiveBan();
            }
        }
        List<AutoMongo> mongos = ReflexBan.select(new Document("uniqueId", uniqueId).append("banned", true), ReflexBan.class);
        for (AutoMongo mongo : mongos) {
            if (mongo != null && mongo instanceof ReflexBan) {
                ReflexBan ban = (ReflexBan) mongo;
                if (ban.isActive()) {
                    cacheBan(ban);
                    return ban;
                }
            }
        }
        return null;
    }

    public ReflexBan getBanById(String id) {
        AutoMongo mongo = ReflexBan.selectOne(new Document("_id", id), ReflexBan.class);
        if (mongo != null && mongo instanceof ReflexBan) {
            ReflexBan ban = (ReflexBan) mongo;
            return ban;
        }
        return null;
    }

    public Set<ReflexBan> getBans(String uniqueId) {
        Set<ReflexBan> bans = new HashSet<>();
        if (cache.containsKey(uniqueId)) {
            bans.addAll(cache.get(uniqueId).getBans());
        }

        List<AutoMongo> mongos = ReflexBan.select(new Document("uniqueId", uniqueId), ReflexBan.class);
        for (AutoMongo mongo : mongos) {
            if (mongo != null && mongo instanceof ReflexBan) {
                ReflexBan ban = (ReflexBan) mongo;
                if (!bans.contains(ban)) {
                    boolean contains = false;
                    for (ReflexBan b : bans) {
                        if (b.getId().equalsIgnoreCase(ban.getId())) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        bans.add(ban);
                    }
                }
            }
        }

        return bans;
    }

    public void cacheBan(ReflexBan ban) {
        if (!cache.containsKey(ban.getUniqueId())) {
            cache.put(ban.getUniqueId(), new BanCache());
        }
        cache.get(ban.getUniqueId()).add(ban);
    }

    public void uncacheBan(ReflexBan ban) {
        cache.remove(ban.getUniqueId());
    }

    public void saveBan(final ReflexBan ban) {
        if (ban.isBanned()) {
            cacheBan(ban);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                ban.update();
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

}
