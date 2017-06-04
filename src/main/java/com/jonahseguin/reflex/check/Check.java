/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.ReflexConfig;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.ban.Autoban;
import com.jonahseguin.reflex.check.violation.CheckViolation;
import com.jonahseguin.reflex.player.reflex.ReflexCache;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private final Reflex reflex;
    private final CheckType checkType;

    @ConfigData("enabled")
    private boolean enabled = true;

    @ConfigData("cancel")
    private boolean cancel = true;

    @ConfigData("ban-freeze")
    private boolean autobanFreeze = true;

    @ConfigData("autoban")
    private boolean autoban = true;

    @ConfigData("infraction-violations")
    private int infractionVL = 5; // Amount of violations before receiving an infraction

    private Set<CheckFail> fails = new HashSet<>();

    public Check(Reflex reflex, CheckType checkType) {
        super(checkType);
        this.reflex = reflex;
        this.checkType = checkType;
    }

    /**
     * Should be overridden by extending Check
     * @return String - the description for this check
     */
    public String description() {
        return "No description available";
    }

    public String getName() {
        return checkType.getName();
    }

    public final Reflex getReflex() {
        return reflex;
    }

    public final ReflexCache getCache() {
        return reflex.getCache();
    }

    public final ReflexConfig getReflexConfig() {
        return reflex.getReflexConfig();
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
                Bukkit.getServer().getPluginManager().registerEvents(this, reflex);
        } else {
            HandlerList.unregisterAll(this);
        }
        this.enabled = enabled;
    }

    public CheckResult fail(ReflexPlayer player, String detail) {
        if (getReflexConfig().isSuppressAlertsOnAutoban() && getReflex().getAutobanManager().hasAutoban(player.getName())) {
            return new CheckResult(getCheckType(), player, null, false, cancel);
        }

        fails.add(new CheckFail(System.currentTimeMillis(), player));

        CheckViolation violation = player.getRecord().addViolation(getCheckType(), detail);

        return new CheckResult(checkType, player, violation, false, this.cancel);
    }

    public Set<CheckFail> getFails() {
        return fails;
    }

    public int getInfractionVL() {
        return infractionVL;
    }

    public void setInfractionVL(int infractionVL) {
        this.infractionVL = infractionVL;
    }

    public boolean isAutoban() {
        return autoban;
    }

    public void setAutoban(boolean autoban) {
        this.autoban = autoban;
        // Setting autoban to false will also remove all active autobans for this check
        if (!autoban) {
            Iterator<String> it = getReflex().getAutobanManager().getAutobans().keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Autoban a = getReflex().getAutobanManager().getAutoban(key);
                if (a != null) {
                    if (a.getCheck().equals(this.getCheckType())) {
                        a.setCancelled(true); // This method also removes the autoban from the cache
                    }
                }
            }
        }
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isAutobanFreeze() {
        return autobanFreeze;
    }

    public void setAutobanFreeze(boolean autobanFreeze) {
        this.autobanFreeze = autobanFreeze;
    }

    public int getTotalRecentFails(int seconds) {
        long period = System.currentTimeMillis() - (seconds * 1000);
        int x = 0;
        for (CheckFail checkFail : fails) {
            if (checkFail.getTime() >= period) {
                x++;
            }
        }
        return x;
    }

}
