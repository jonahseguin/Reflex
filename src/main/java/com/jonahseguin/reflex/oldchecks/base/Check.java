/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.base;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexCache;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * The Basic, bare-bones Check superclass
 * The 3 different "Check Types" extend this class: Trigger, Capture (data), and Inspect
 * Basic Flowchart for this is Trigger a capture -> Capture period, collect data -> Inspect data collected from capture period
 * <p>
 * This also implements Bukkit's Listener and is automatically registered when the oldchecks is set to enabled.
 * And vice-versa when the oldchecks is disabled.
 * <p>
 * CheckType representing the type of Check it is; or the Hack it is checking for
 * RCheckType representing which of the 3 Check Types or stages it is, Trigger, Capture, or Inspect
 * <p>
 * Each type has their own Manager and implementation in their respective packages
 */
public abstract class Check extends CheckConfig implements Listener {

    private final Reflex instance;

    private final CheckType checkType;
    private final RCheckType rCheckType;

    @ConfigData("enabled")
    private boolean enabled = true;

    public Check(Reflex instance, CheckType checkType, RCheckType rCheckType) {
        super(checkType, rCheckType);
        this.instance = instance;
        this.checkType = checkType;
        this.rCheckType = rCheckType;
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

    public final RCheckType getRCheckType() {
        return rCheckType;
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
}
