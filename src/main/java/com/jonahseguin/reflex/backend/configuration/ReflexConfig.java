/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.backend.configuration;

import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigSerializer;
import com.jonahseguin.reflex.util.obj.AutobanMethod;
import com.jonahseguin.reflex.util.serial.AutobanMethodSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

@Getter
@Setter
public class ReflexConfig extends Configuration {

    @ConfigData("ban.method")
    @ConfigSerializer(serializer = AutobanMethodSerializer.class)
    private AutobanMethod autobanMethod = AutobanMethod.REFLEX;
    @ConfigData("ban.time")
    private int autobanTime = 60;
    @ConfigData("ban.remind-interval")
    private int autobanRemindInterval = 15;
    @ConfigData("ban.console.command")
    private String autobanConsoleCommand = "ban {0} [Reflex] Hacking ({1})";
    @ConfigData("ban.tempban-time-minutes")
    private int autobanTimeMinutes = 1440;//1 day
    @ConfigData("alerts.suppress-on-autoban")
    private boolean suppressAlertsOnAutoban = true;
    @ConfigData("alerts.grouping-interval-seconds")
    private int alertGroupingIntervalSeconds = 5;
    @ConfigData("alerts.max-alerts-per-player-per-second")
    private int maxAlertsPPPS = 2;
    @ConfigData("infraction.cache-expiry-minutes")
    private int violationCacheExpiryMinutes = 120; // 2 hours
    @ConfigData("player.join-timeout-seconds")
    private int joinTimeoutSeconds = 3;

    // Hack Chance
    @ConfigData("hackchance.infractions.minimum")
    private int hackChanceInfractionsMinimum = 1;

    @ConfigData("hackchance.validReflexBans.minimum")
    private int hackChanceValidReflexBansMinimum = 1;

    @ConfigData("hackchance.violations.minimum")
    private int hackChanceViolationsMinimum = 4;

    @ConfigData("hackchance.sessionvl.minimum")
    private int hackChanceSessionVLMinimum = 7;

    @ConfigData("hackchance.totalvl.minimum")
    private int hackChanceTotalVLMinimum = 5;

    @ConfigData("hackchance.failurefrequency.minimum")
    private int hackChanceFailureFrequencyMinimum = 3; // 3 per hour

    @ConfigData("hackchance.globalrecentfails.maximum")
    private int hackChanceGlobalRecentFailsMaximum = 5;

    @ConfigData("hackchance.pingspike.maximum")
    private int hackChancePingSpikeMaximum = 35;

    @ConfigData("hackchance.tpsspike.maxiumum")
    private double hackChanceTpsSpikeMaximum = 2.5;

    public ReflexConfig(Plugin plugin) {
        super(plugin);
        load();
        save();
        saveDefaults();
    }

}
