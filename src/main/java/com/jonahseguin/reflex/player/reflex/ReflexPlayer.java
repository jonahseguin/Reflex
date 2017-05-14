/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.player.reflex;

import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.jonahseguin.reflex.backend.database.mongo.annotations.MongoColumn;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.check.PlayerData;
import com.jonahseguin.reflex.check.alert.PlayerAlerts;
import com.jonahseguin.reflex.check.violation.PlayerRecord;
import com.jonahseguin.reflex.player.cache.CachePlayer;
import com.jonahseguin.reflex.util.serial.PlayerRecordSerializer;
import lombok.*;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@AllArgsConstructor
@CollectionName(name = "reflex_players")
@Getter
@Setter
public class ReflexPlayer extends CachePlayer {

    //Non-persistent...
    private final PlayerAlerts alerts = new PlayerAlerts(this);
    @MongoColumn(name = "username")
    @NonNull
    private String name;
    @MongoColumn(name = "uuid", identifier = true)
    @NonNull
    private String uniqueId;
    @MongoColumn(name = "record")
    @DatabaseSerializer(serializer = PlayerRecordSerializer.class)
    private PlayerRecord record = new PlayerRecord(this);
    private int sessionVL = 0;
    private Player bukkitPlayer = null;
    private boolean alertsEnabled = true;
    private boolean online = false;
    private long lastAlertTime = System.currentTimeMillis();
    private PlayerData data = null;
    private long joinTimeout = 0; // Cooldown period where all checks are ignored during login

    public ReflexPlayer() { //So that AutoMongo can instantiate without throwing an InstantiationException

    }

    public boolean canCheck() {
        return joinTimeout < System.currentTimeMillis();
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


    public void addPreVL(CheckType checkType) {
        int vl = getPreVL(checkType);
        this.record.getPreVL().put(checkType, (vl + 1));
    }

    public void modifyPreVL(CheckType checkType, int change) {
        int vl = getPreVL(checkType);
        vl += change;
        if (vl < 0) {
            vl = 0;
        }
        this.record.getPreVL().put(checkType, vl);
    }

    public void setPreVL(CheckType checkType, int val) {
        this.record.getPreVL().put(checkType, val);
    }

    public int getPreVL(CheckType checkType) {
        if (!record.getPreVL().containsKey(checkType)) {
            record.getPreVL().put(checkType, 0);
        }
        return record.getPreVL().get(checkType);
    }

    public int getPing() {
        return ((CraftPlayer) bukkitPlayer).getHandle().ping;
    }

}
