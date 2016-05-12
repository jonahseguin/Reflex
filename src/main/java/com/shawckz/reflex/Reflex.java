/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.shawckz.reflex.core.cache.CachePlayer;
import com.shawckz.reflex.core.cmd.CommandHandler;
import com.shawckz.reflex.core.configuration.LanguageConfig;
import com.shawckz.reflex.core.configuration.ReflexConfig;
import com.shawckz.reflex.core.database.DBManager;
import com.shawckz.reflex.core.player.ReflexCache;
import com.shawckz.reflex.prevent.check.CheckManager;
import com.shawckz.reflex.util.Lag;
import com.shawckz.reflex.util.ReflexTimer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Reflex extends JavaPlugin {


    @Getter private static Reflex instance;
    @Getter private ReflexConfig reflexConfig;
    @Getter private ProtocolManager protocolManager;
    @Getter private CommandHandler commandHandler;
    @Getter private LanguageConfig lang;

    public static String getPrefix(){
        return ChatColor.translateAlternateColorCodes('&', instance.getReflexConfig().getPrefix());
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable(){
        instance = this;

        reflexConfig = new ReflexConfig(this);

        lang = new LanguageConfig(this);

        new DBManager(this);

        commandHandler = new CommandHandler(this);

        //Make reload-friendly, load all online players
        for(Player pl : Bukkit.getOnlinePlayers()){
            CachePlayer cp = ReflexCache.get().loadCachePlayer(pl.getName());
            if(cp != null){
                ReflexCache.get().put(cp);
            }
            else{
                cp = ReflexCache.get().create(pl.getName(), pl.getUniqueId().toString());
                ReflexCache.get().put(cp);
                cp.update();
            }
        }

        CheckManager.get().setupChecks();//Setup
        ReflexCache.get();//Setup
        Bukkit.getScheduler().runTaskTimer(this, new ReflexTimer(),20L,20L);

        Bukkit.getScheduler().runTaskTimer(this, new Lag(), 1L, 1L);
    }

    @Override
    public void onDisable(){

        //Make reload-friendly, save all online players
        for(Player pl : Bukkit.getOnlinePlayers()){
            ReflexCache.get().save(pl);
        }

        reflexConfig.save();
        reflexConfig = null;
        instance = null;
    }

}
