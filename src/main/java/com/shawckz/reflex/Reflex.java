/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.shawckz.reflex.cache.CachePlayer;
import com.shawckz.reflex.check.CheckManager;
import com.shawckz.reflex.cmd.CommandHandler;
import com.shawckz.reflex.configuration.ReflexConfig;
import com.shawckz.reflex.database.DBManager;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.util.Lag;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Reflex extends JavaPlugin {

    /**
     * IDEA
     * <p>
     * Have base tester checks; when plugin suspects player might be a hacker:
     * send to be tested for a period of time and log results,
     * keep doing this until the plugin is certain and makes a decision or asks for staff help
     */

    @Getter private static Plugin plugin;
    @Getter private static ReflexConfig reflexConfig;

    @Getter private static ProtocolManager protocolManager;

    @Getter private static CommandHandler commandHandler;

    public static String getPrefix(){
        return ChatColor.translateAlternateColorCodes('&', getReflexConfig().getPrefix());
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable(){
        plugin = this;

        reflexConfig = new ReflexConfig(this);

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
        plugin = null;
    }

}
