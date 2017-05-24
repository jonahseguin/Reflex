/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.jonahseguin.reflex.backend.command.RCommandHandler;
import com.jonahseguin.reflex.backend.configuration.LanguageConfig;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexConfig;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.database.DBManager;
import com.jonahseguin.reflex.ban.AutobanManager;
import com.jonahseguin.reflex.ban.ReflexBanManager;
import com.jonahseguin.reflex.check.CheckManager;
import com.jonahseguin.reflex.check.ReflexTimer;
import com.jonahseguin.reflex.check.alert.AlertManager;
import com.jonahseguin.reflex.check.violation.ViolationCache;
import com.jonahseguin.reflex.commands.*;
import com.jonahseguin.reflex.listener.BanListener;
import com.jonahseguin.reflex.listener.BukkitListener;
import com.jonahseguin.reflex.listener.PacketListener;
import com.jonahseguin.reflex.player.reflex.ReflexCache;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.menu.MenuListener;
import com.jonahseguin.reflex.util.obj.Lag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * Reflex - AntiCheat for Minecraft Servers
 * https://shawckz.com/product/reflex
 *
 * @author Jonah Seguin
 * @version 2.0.1BETA1 (${project.version})
 * @since 1.0.0
 */
public class Reflex extends JavaPlugin {

    private static Reflex instance;
    private ReflexConfig reflexConfig;
    private ProtocolManager protocolManager;
    private LanguageConfig lang;
    private ReflexCache cache;
    private ReflexTimer reflexTimer;
    private AutobanManager autobanManager;
    private ReflexBanManager banManager;
    private RCommandHandler commandHandler;
    private AlertManager alertManager;
    private ViolationCache violationCache;
    private CheckManager checkManager;

    public static void log(String msg) {
        getInstance().getLogger().info("[Reflex] " + msg);
    }

    public static String getPrefix() {
        return RLang.format(ReflexLang.ALERT_PREFIX);
    }

    public static Reflex getInstance() {
        return instance;
    }

    public static Set<ReflexPlayer> getReflexPlayers() {
        return getInstance().getCache().getOnlineReflexPlayers();
    }

    public static Set<Player> getOnlinePlayers() {
        return getInstance().getCache().getOnlinePlayers();
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        getLogger().info("[Start] Starting Reflex v" + getDescription().getVersion() + " by Jonah Seguin (Shawckz).");
        instance = this;
        reflexConfig = new ReflexConfig(instance);
        lang = new LanguageConfig(instance);
        new DBManager(instance);
        cache = new ReflexCache(instance);

        //Make reload-friendly, load all online players
        for (Player pl : getOnlinePlayers()) {
            ReflexPlayer cp = cache.loadReflexPlayer(pl.getName());
            if (cp != null) {
                cache.put(cp);
            } else {
                cp = cache.create(pl.getName(), pl.getUniqueId().toString());
                cache.put(cp);
                cp.update();
            }
        }

        autobanManager = new AutobanManager();
        alertManager = new AlertManager(instance);
        violationCache = new ViolationCache(instance);

        reflexTimer = new ReflexTimer(instance);

        // Check setup
        checkManager = new CheckManager(this);

        banManager = new ReflexBanManager();

        // Commands
        commandHandler = new RCommandHandler(instance);
        commandHandler.registerCommands(new CmdReflex());
        commandHandler.registerCommands(new CmdCancel());
        commandHandler.registerCommands(new CmdLookup());
        commandHandler.registerCommands(new CmdBan());
        commandHandler.registerCommands(new CmdSettings());
        commandHandler.registerCommands(new CmdConfig());
        commandHandler.registerCommands(new CmdCheck());
        commandHandler.registerCommands(new CmdAlert());

        getServer().getPluginManager().registerEvents(new BanListener(), instance);
        getServer().getPluginManager().registerEvents(new BukkitListener(instance), instance);

        MenuListener.getInstance().register(instance);

        new PacketListener(instance);

        Bukkit.getScheduler().runTaskTimer(instance, new Lag(), 1L, 1L);
        getLogger().info("[Start] [Finish] Enabled Reflex v" + getDescription().getVersion() + " by Jonah Seguin (Shawckz).");

    }

    @Override
    public void onDisable() {
        getLogger().info("[Stop] Disabling Reflex v" + getDescription().getVersion());
        // Make reload-friendly, save all online players
        int saved = 0;
        for (Player pl : getOnlinePlayers()) {
            cache.saveSync(pl);
            saved++;
        }
        cache.clear();
        getLogger().info("[Stop] Saved " + saved + " players.");

        reflexTimer.clear();
        reflexConfig.save();
        reflexConfig = null;
        commandHandler = null;
        banManager = null;
        reflexTimer = null;
        lang = null;
        cache = null;
        checkManager.save();
        checkManager = null;
        instance = null;

        getLogger().info("[Stop] [Finish] Disabled Reflex v" + getDescription().getVersion() + " by Jonah Seguin (Shawckz).");
    }

    public RCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public ReflexBanManager getBanManager() {
        return banManager;
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

    public ReflexCache getCache() {
        return cache;
    }

    public ReflexTimer getReflexTimer() {
        return reflexTimer;
    }

    public AutobanManager getAutobanManager() {
        return autobanManager;
    }

    public AlertManager getAlertManager() {
        return alertManager;
    }

    public ViolationCache getViolationCache() {
        return violationCache;
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }
}
