/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.shawckz.reflex.auth.AuthCaller;
import com.shawckz.reflex.auth.AuthMe;
import com.shawckz.reflex.auth.ShawXAuth;
import com.shawckz.reflex.backend.command.RCommandHandler;
import com.shawckz.reflex.backend.configuration.LanguageConfig;
import com.shawckz.reflex.backend.configuration.RLang;
import com.shawckz.reflex.backend.configuration.ReflexConfig;
import com.shawckz.reflex.backend.configuration.ReflexLang;
import com.shawckz.reflex.backend.database.DBManager;
import com.shawckz.reflex.ban.AutobanManager;
import com.shawckz.reflex.ban.ReflexBanManager;
import com.shawckz.reflex.check.base.RDataCache;
import com.shawckz.reflex.check.base.ReflexTimer;
import com.shawckz.reflex.check.data.DataCaptureManager;
import com.shawckz.reflex.check.inspect.InspectManager;
import com.shawckz.reflex.check.trigger.TriggerManager;
import com.shawckz.reflex.commands.*;
import com.shawckz.reflex.listener.BanListener;
import com.shawckz.reflex.listener.PacketListener;
import com.shawckz.reflex.player.reflex.ReflexCache;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Lag;
import com.shawckz.reflex.util.utility.ReflexException;
import ninja.amp.ampmenus.MenuListener;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Reflex - AntiCheat for Minecraft Servers
 * https://shawckz.com/product/reflex
 *
 * @author Jonah Seguin (Shawckz)
 * @version 1.0.0 (${project.version})
 * @since 1.0.0
 */
public class Reflex extends AuthMe {

    public static boolean couldStart = true;

    private static Reflex instance;

    private ReflexConfig reflexConfig;
    private ProtocolManager protocolManager;
    private LanguageConfig lang;

    private TriggerManager triggerManager;
    private DataCaptureManager dataCaptureManager;
    private InspectManager inspectManager;

    private ReflexCache cache;
    private ReflexTimer reflexTimer;
    private RDataCache violationCache;

    private AutobanManager autobanManager;
    private ReflexBanManager banManager;

    private RCommandHandler commandHandler;
    private boolean en = false;
    private boolean authenticated = false;

    public static String getPrefix() {
        return RLang.format(ReflexLang.ALERT_PREFIX);
    }

    public static Reflex getInstance() {
        return instance;
    }

    public static Set<ReflexPlayer> getOnlinePlayers() {
        return getInstance().getCache().getOnlinePlayers();
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        getLogger().info("[Start] Starting Reflex v" + getDescription().getVersion());
        instance = this;

        getLogger().info("[Auth] Authenticating");

        ShawXAuth.auth(this);
    }

    @Override
    public void onDisable() {
        if (en) {
            getLogger().info("[Stop] Disabling Reflex v" + getDescription().getVersion());
            //Make reload-friendly, save all online players
            int saved = 0;
            for (Player pl : Bukkit.getOnlinePlayers()) {
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
            inspectManager = null;
            dataCaptureManager = null;
            triggerManager = null;
            reflexTimer = null;
            lang = null;
            cache = null;
            instance = null;

            getLogger().info("[Finish] Reflex v" + getDescription().getVersion() + " by Shawckz.");
        }
    }

    @Override
    public final AuthCaller auth(ShawXAuth autheer) {
        if (autheer == null) throw new ReflexException("Invalid autheer");
        return () -> {
            en = true;
            authenticated = true;
            reflexConfig = new ReflexConfig(instance);
            lang = new LanguageConfig(instance);
            new DBManager(instance);
            cache = new ReflexCache(instance);

            //Make reload-friendly, load all online players
            for (Player pl : Bukkit.getOnlinePlayers()) {
                ReflexPlayer cp = cache.loadReflexPlayer(pl.getName());
                if (cp != null) {
                    cache.put(cp);
                }
                else {
                    cp = cache.create(pl.getName(), pl.getUniqueId().toString());
                    cache.put(cp);
                    cp.update();
                }
            }

            violationCache = new RDataCache();

            autobanManager = new AutobanManager();

            reflexTimer = new ReflexTimer(instance);

            //Setup triggers, data captures, inspectors
            triggerManager = new TriggerManager(instance);
            triggerManager.setup();
            dataCaptureManager = new DataCaptureManager(instance);
            dataCaptureManager.setup();
            inspectManager = new InspectManager(instance);
            inspectManager.setup();

            banManager = new ReflexBanManager();

            //Commands
            commandHandler = new RCommandHandler(instance);
            commandHandler.registerCommands(new CmdReflex());
            commandHandler.registerCommands(new CmdCancel());
            commandHandler.registerCommands(new CmdInspect());
            commandHandler.registerCommands(new CmdLookup());
            commandHandler.registerCommands(new CmdBan());
            commandHandler.registerCommands(new CmdSettings());
            commandHandler.registerCommands(new CmdConfig());

            getServer().getPluginManager().registerEvents(new BanListener(), instance);

            MenuListener.getInstance().register(instance);

            new PacketListener(instance);

            Bukkit.getScheduler().runTaskTimer(instance, new Lag(), 1L, 1L);
            getLogger().info("[Finish] Reflex v" + getDescription().getVersion() + " by Shawckz.");
        };
    }

    @Override
    public final String authName() {
        return "Reflex";
    }

    public boolean isAuthenticated() {
        return authenticated;
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

    public RDataCache getViolationCache() {
        return violationCache;
    }

    public AutobanManager getAutobanManager() {
        return autobanManager;
    }
}
