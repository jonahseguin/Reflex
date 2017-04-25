/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;
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
 *
 * The Check superclass;  All checks inherit from this class
 *
 * Implements Bukkit's Listener and is automatically registered/unregistered on enable/disable
 *
 * Managed in the CheckManager
 *
 * Each check has a CheckType
 *
 */
public abstract class Check extends CheckConfig implements Listener {

    private final Reflex instance;
    private final CheckType checkType;

    @ConfigData("enabled")
    private boolean enabled = false;

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
        }
        else {
            HandlerList.unregisterAll(this);
        }
        this.enabled = enabled;
    }
}
