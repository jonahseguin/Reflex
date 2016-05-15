/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.shawckz.reflex.autoban.AutobanManager;
import com.shawckz.reflex.backend.command.RCommandHandler;
import com.shawckz.reflex.backend.configuration.LanguageConfig;
import com.shawckz.reflex.backend.configuration.ReflexConfig;
import com.shawckz.reflex.backend.database.DBManager;
import com.shawckz.reflex.check.base.ReflexTimer;
import com.shawckz.reflex.check.base.ViolationCache;
import com.shawckz.reflex.check.data.DataCaptureManager;
import com.shawckz.reflex.check.inspect.InspectManager;
import com.shawckz.reflex.check.trigger.TriggerManager;
import com.shawckz.reflex.commands.CmdCancel;
import com.shawckz.reflex.commands.CmdReflex;
import com.shawckz.reflex.player.cache.CachePlayer;
import com.shawckz.reflex.player.reflex.ReflexCache;
import com.shawckz.reflex.util.obj.Lag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * Reflex - AntiCheat for Minecraft Servers
 * http://shawckz.com/project/reflex
 * @author Jonah Seguin (Shawckz)
 * @version 1.0.0 (${project.version})
 * @since 1.0.0
 *
 */
public class Reflex extends JavaPlugin {

    private static Reflex instance;

    private ReflexConfig reflexConfig;
    private ProtocolManager protocolManager;
    private LanguageConfig lang;

    private TriggerManager triggerManager;
    private DataCaptureManager dataCaptureManager;
    private InspectManager inspectManager;

    private ReflexCache cache;
    private ReflexTimer reflexTimer;
    private ViolationCache violationCache;

    private AutobanManager autobanManager;

    public static String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', instance.getReflexConfig().getPrefix());
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        getLogger().info("[Start] Starting Reflex v" + getDescription().getVersion());
        instance = this;


        reflexConfig = new ReflexConfig(this);
        lang = new LanguageConfig(this);
        new DBManager(this);
        cache = new ReflexCache(this);

        //Make reload-friendly, load all online players
        for (Player pl : Bukkit.getOnlinePlayers()) {
            CachePlayer cp = cache.loadCachePlayer(pl.getName());
            if (cp != null) {
                cache.put(cp);
            }
            else {
                cp = cache.create(pl.getName(), pl.getUniqueId().toString());
                cache.put(cp);
                cp.update();
            }
        }

        violationCache = new ViolationCache();

        autobanManager = new AutobanManager();

        reflexTimer = new ReflexTimer(this);

        //Setup triggers, data captures, inspectors
        triggerManager = new TriggerManager(this);
        triggerManager.setup();
        dataCaptureManager = new DataCaptureManager(this);
        dataCaptureManager.setup();
        inspectManager = new InspectManager(this);
        inspectManager.setup();

        RCommandHandler commandHandler = new RCommandHandler(this);
        commandHandler.registerCommands(new CmdReflex());
        commandHandler.registerCommands(new CmdCancel());

        Bukkit.getScheduler().runTaskTimer(this, new Lag(), 1L, 1L);


        getLogger().info("[Finish] Reflex v" + getDescription().getVersion() + " by Shawckz.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[Stop] Disabling Reflex v" + getDescription().getVersion());
        //Make reload-friendly, save all online players
        int saved = 0;
        for (Player pl : Bukkit.getOnlinePlayers()) {
            cache.saveSync(pl);
            saved++;
        }
        getLogger().info("[Stop] Saved " + saved + " players.");

        reflexConfig.save();
        reflexConfig = null;
        instance = null;

        getLogger().info("[Finish] Reflex v" + getDescription().getVersion() + " by Shawckz.");
    }

    public static Reflex getInstance() {
        return instance;
    }

    public ReflexConfig getReflexConfig() {
        return reflexConfig;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public LanguageConfig getLang() {
        return lang;
    }

    public TriggerManager getTriggerManager() {
        return triggerManager;
    }

    public ReflexCache getCache() {
        return cache;
    }

    public DataCaptureManager getDataCaptureManager() {
        return dataCaptureManager;
    }

    public ReflexTimer getReflexTimer() {
        return reflexTimer;
    }

    public InspectManager getInspectManager() {
        return inspectManager;
    }

    public ViolationCache getViolationCache() {
        return violationCache;
    }

    public AutobanManager getAutobanManager() {
        return autobanManager;
    }
}
