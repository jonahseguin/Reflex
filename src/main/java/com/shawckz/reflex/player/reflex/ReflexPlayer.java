/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.player.reflex;

import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.backend.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.data.PlayerData;
import com.shawckz.reflex.check.data.RCapturePlayer;
import com.shawckz.reflex.player.cache.CachePlayer;
import lombok.*;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

@RequiredArgsConstructor
@AllArgsConstructor
@CollectionName(name = "reflex_players")
@Getter
@Setter
public class ReflexPlayer extends CachePlayer {

    @MongoColumn(name = "username")
    @NonNull
    private String name;

    @MongoColumn(name = "uuid", identifier = true)
    @NonNull
    private String uniqueId;

    private int sessionVL = 0;

    //Non-persistent...
    private Map<String, Integer> vl = new HashMap<>();

    private final PlayerData data = new PlayerData();

    private final RCapturePlayer capturePlayer = new RCapturePlayer(this);

    @Getter
    @Setter
    private Player bukkitPlayer = null;
    @Getter
    @Setter
    private boolean alertsEnabled = true;

    public ReflexPlayer() {//So that AutoMongo can instantiate without throwing an InstantiationException
    }


    public void msg(String msg) {
        bukkitPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public PlayerData getData() {
        return data;
    }

    public void addVL(CheckType checkType) {
        int vl = getVL(checkType);
        this.vl.put(checkType.getName(), (vl + 1));
        sessionVL++;
    }

    public int getVL(CheckType checkType) {
        if (!vl.containsKey(checkType.getName())) {
            vl.put(checkType.getName(), 0);
        }
        return vl.get(checkType.getName());
    }

}