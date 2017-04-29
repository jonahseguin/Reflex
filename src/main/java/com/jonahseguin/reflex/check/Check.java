/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.ReflexConfig;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.oldchecks.base.CheckConfig;
import com.jonahseguin.reflex.player.reflex.ReflexCache;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 19:53.
 * Project: Reflex
 * <p>
 * The Check superclass;  All checks inherit from this class
 * <p>
 * Implements Bukkit's Listener and is automatically registered/unregistered on enable/disable
 * <p>
 * Managed in the CheckManager
 * <p>
 * Each check has a CheckType
 */
public abstract class Check extends CheckConfig implements Listener {

    private final Reflex instance;
    private final CheckType checkType;

    @ConfigData("enabled")
    private boolean enabled = false;

    @ConfigData("cancel")
    private boolean cancel = true;

    @ConfigData("ban-freeze")
    private boolean autobanFreeze = true;

    @ConfigData("autoban")
    private boolean autoban = true;

    public Check(Reflex instance, CheckType checkType) {
        super(checkType);
        this.instance = instance;
        this.checkType = checkType;
    }

    public String getName() {
        return checkType.getName();
    }

    public final Reflex getReflex() {
        return instance;
    }

    public final ReflexCache getCache() {
        return instance.getCache();
    }

    public final ReflexConfig getReflexConfig() {
        return instance.getReflexConfig();
    }

    public final ReflexPlayer getPlayer(Player player) {
        return getPlayerByUniqueID(player.getUniqueId().toString());
    }

    public final ReflexPlayer getPlayerByName(String name) {
        return getCache().getReflexPlayer(name);
    }

    public final ReflexPlayer getPlayerByUniqueID(String uniqueId) {
        return getCache().getReflexPlayerByUniqueId(uniqueId);
    }

    public final CheckType getCheckType() {
        return checkType;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(final boolean enabled) {
        if (enabled) {
            Bukkit.getServer().getPluginManager().registerEvents(this, instance);
        } else {
            HandlerList.unregisterAll(this);
        }
        this.enabled = enabled;
    }

    public CheckResult fail(ReflexPlayer player, String... detail) {
        if (getReflexConfig().isSuppressAlertsOnAutoban() && getReflex().getAutobanManager().hasAutoban(player.getName())) {
            return new CheckResult(getCheckType(), player, null, false, cancel);
        }

        String d = null;
        if (detail != null && detail.length > 0) {
            d = detail[0];
        }

        player.addVL(getCheckType());


        long violationExpiry = System.currentTimeMillis() + (getReflexConfig().getViolationCacheExpiryMinutes() * 60 * 1000);
        CheckViolation violation = new CheckViolation(player, System.currentTimeMillis(), violationExpiry, getCheckType(), player.getVL(getCheckType()));
        //TODO: Save to ViolationCache


        //TODO
    }

}
