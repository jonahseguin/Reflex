/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.player.reflex;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.RLang;
import com.shawckz.reflex.backend.configuration.ReflexLang;
import com.shawckz.reflex.backend.configuration.ReflexPerm;
import com.shawckz.reflex.player.cache.AbstractCache;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

/**
 * Created by Jonah on 6/15/2015.
 */
public class ReflexCache extends AbstractCache {

    public ReflexCache(Reflex plugin) {
        super(plugin);
    }

    //See superclass for documentation
    public ReflexPlayer getReflexPlayer(String name) {
        ReflexPlayer cachePlayer = getBasePlayer(name);
        if (cachePlayer != null) {
            return getBasePlayer(name);
        }
        return null;
    }

    public ReflexPlayer getReflexPlayerByUUID(String uuid) {
        ReflexPlayer cachePlayer = getBasePlayerByUUID(uuid);
        if (cachePlayer != null) {
            return getBasePlayerByUUID(uuid);
        }
        return null;
    }

    //See superclass for documentation
    public ReflexPlayer getReflexPlayer(Player p) {
        if (p != null) {
            return getReflexPlayer(p.getName());
        }
        return null;
    }

    public Set<ReflexPlayer> getOnlineReflexPlayers() {
        return getPlayers().values().stream().filter(ReflexPlayer::isOnline).collect(Collectors.toSet());
    }

    @Override
    public ReflexPlayer create(String name, String uuid) {
        return new ReflexPlayer(name, uuid);
    }

    @Override
    public void init(Player p, ReflexPlayer reflexPlayer) {
        reflexPlayer.setBukkitPlayer(p);
        if (ReflexPerm.ALERTS.hasPerm(p)) {
            reflexPlayer.setAlertsEnabled(true);
            RLang.send(p, ReflexLang.ALERTS_ENABLED);
        }
    }
}
