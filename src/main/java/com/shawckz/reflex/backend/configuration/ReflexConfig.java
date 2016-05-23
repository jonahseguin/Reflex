/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.backend.configuration;

import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.backend.configuration.annotations.ConfigSerializer;
import com.shawckz.reflex.util.obj.AutobanMethod;
import com.shawckz.reflex.util.serial.AutobanMethodSerializer;
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

    public ReflexConfig(Plugin plugin) {
        super(plugin);
        load();
        save();
    }

}
