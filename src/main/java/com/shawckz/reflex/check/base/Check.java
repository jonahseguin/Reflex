/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.base;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * The Basic, bare-bones Check superclass
 * The 3 different "Check Types" extend this class: Trigger, Capture (data), and Inspect
 * Basic Flowchart for this is Trigger a capture -> Capture period, collect data -> Inspect data collected from capture period
 *
 * This also implements Bukkit's Listener and is automatically registered when the check is set to enabled.
 * And vice-versa when the check is disabled.
 *
 * CheckType representing the type of Check it is; or the Hack it is checking for
 * RCheckType representing which of the 3 Check Types or stages it is, Trigger, Capture, or Inspect
 *
 * Each type has their own Manager and implementation in their respective packages
 */
public abstract class Check extends CheckConfig implements Listener {

    private final CheckType checkType;
    private final RCheckType rCheckType;

    @ConfigData("enabled")
    private boolean enabled = true;

    public Check(CheckType checkType, RCheckType rCheckType) {
        super(checkType, rCheckType);
        this.checkType = checkType;
        this.rCheckType = rCheckType;
    }

    //Can be overriden
    public String getName() {
        return checkType.getName();
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public RCheckType getrCheckType() {
        return rCheckType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(final boolean enabled) {
        if (enabled) {
            Bukkit.getServer().getPluginManager().registerEvents(this, Reflex.getInstance());
        }
        else {
            HandlerList.unregisterAll(this);
        }
        this.enabled = enabled;
    }
}
