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

    public ReflexConfig(Plugin plugin) {
        super(plugin);
        load();
        save();
    }

    @ConfigData("enabled")
    private boolean enabled = true;
    @ConfigData("alert-format-detail")
    private String alertFormatDetail = "&7[&c&lAres&r&7] %player&7(&c%totalvlxVL&7)&r &7failed &9%check" +
            "&7(%detail&7)[&c%vlVL&7]";
    @ConfigData("alert-format")
    private String alertFormat = "&7[&c&lAres&r&7] %player&7(&c%totalvlxVL&7)&r &7failed &9%check" +
            "&7[&c%vlVL&7]";

    @ConfigData("prefix")
    private String prefix = "&7[&c!&7]";


    @ConfigData("ban.method")
    @ConfigSerializer(serializer = AutobanMethodSerializer.class)
    private AutobanMethod autobanMethod = AutobanMethod.REFLEX;

    @ConfigData("ban.time")
    private int autobanTime = 60;

    @ConfigData("ban.remind-interval")
    private int autobanRemindInterval = 15;

    @ConfigData("ban.console.command")
    private String autobanConsoleCommand = "ban {0} [Reflex] Hacking ({1})";


}
