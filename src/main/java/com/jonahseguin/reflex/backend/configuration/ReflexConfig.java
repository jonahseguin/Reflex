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

    @ConfigData("debug")
    private boolean debug = false;

    @ConfigData("ban.method")
    @ConfigSerializer(serializer = AutobanMethodSerializer.class)
    private AutobanMethod autobanMethod = AutobanMethod.REFLEX;

    @ConfigData("ban.time.staff-online")
    private int autobanTimeStaffOnline = 300; // Seconds

    @ConfigData("ban.time.no-staff")
    private int autobanTimeNoStaff = 180;

    @ConfigData("ban.remind-interval")
    private int autobanRemindInterval = 30;

    @ConfigData("ban.console.command")
    private String autobanConsoleCommand = "ban {0} [Reflex] Hacking ({1})";

    @ConfigData("ban.tempban-time-minutes-scaling")
    private int[] scalingAutobanTimeMinutes = new int[]{180, 1440, 4320, 10080}; // 3 hours, 1 day, 3 days, 1 week

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
    private int hackChanceGlobalRecentFailsMaximum = 7;

    @ConfigData("hackchance.pingspike.maximum")
    private int hackChancePingSpikeMaximum = 35;

    @ConfigData("hackchance.tpsspike.maxiumum")
    private double hackChanceTpsSpikeMaximum = 2.5;

    @ConfigData("slack.enable-hook")
    private boolean slackHook = false;

    @ConfigData("slack.uri")
    private String slackHookURI = "https://hooks.slack.com/services/ID-1/ID-2/ID-3";

    @ConfigData("slack.destination-channel")
    private String slackDestinationChannel = "#reflex";

    public ReflexConfig(Plugin plugin) {
        super(plugin);
        saveDefaults();
        load();
        save();
    }

}
