/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.player.reflex;

import com.google.common.collect.Maps;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.backend.database.mongo.annotations.MongoColumn;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.data.RCapturePlayer;
import com.jonahseguin.reflex.player.cache.CachePlayer;
import com.jonahseguin.reflex.oldchecks.data.PlayerData;
import lombok.*;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@AllArgsConstructor
@CollectionName(name = "reflex_players")
@Getter
@Setter
public class ReflexPlayer extends CachePlayer {

    private final PlayerData data = new PlayerData();
    private final RCapturePlayer capturePlayer = new RCapturePlayer(this);
    @MongoColumn(name = "username")
    @NonNull
    private String name;
    @MongoColumn(name = "uuid", identifier = true)
    @NonNull
    private String uniqueId;
    //Non-persistent...
    private int sessionVL = 0;
    private Map<String, Integer> vl = Maps.newHashMap(); //Violation Level for each oldchecks, <CheckType#toString, VL>
    private Map<String, Integer> alertVL = Maps.newHashMap();//Pre-Failure VLs for each oldchecks, <CheckType#toString, VL>
    private Player bukkitPlayer = null;
    private boolean alertsEnabled = true;
    private boolean online = false;

    public ReflexPlayer() { //So that AutoMongo can instantiate without throwing an InstantiationException

    }


    public void msg(String msg) {
        bukkitPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void msg(ReflexLang lang, String... args) {
        RLang.send(bukkitPlayer, lang, args);
    }

    public boolean hasPermission(ReflexPerm perm) {
        return bukkitPlayer != null && bukkitPlayer.isOnline() && perm.hasPerm(bukkitPlayer);
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

    public boolean hasVL(CheckType checkType) {
        return vl.containsKey(checkType.getName()) && getVL(checkType) > 0;
    }

    public void modifyVL(CheckType checkType, int change) {
        int vl = getVL(checkType);
        vl += change;
        if (vl < 0) {
            vl = 0;
        }
        this.vl.put(checkType.getName(), vl);
    }

    public void setVL(CheckType checkType, int val) {
        this.vl.put(checkType.getName(), val);
    }

    public void addAlertVL(CheckType checkType) {
        int vl = getAlertVL(checkType);
        this.alertVL.put(checkType.getName(), (vl + 1));
    }

    public void modifyAlertVL(CheckType checkType, int change) {
        int vl = getAlertVL(checkType);
        vl += change;
        if (vl < 0) {
            vl = 0;
        }
        this.alertVL.put(checkType.getName(), vl);
    }

    public void setAlertVL(CheckType checkType, int val) {
        this.alertVL.put(checkType.getName(), val);
    }

    public int getAlertVL(CheckType checkType) {
        if (!alertVL.containsKey(checkType.getName())) {
            alertVL.put(checkType.getName(), 0);
        }
        return alertVL.get(checkType.getName());
    }

}
