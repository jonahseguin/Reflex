package com.shawckz.reflex.core.configuration;

import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.configuration.annotations.ConfigData;
import com.shawckz.reflex.core.configuration.annotations.ConfigSerializer;
import com.shawckz.reflex.core.database.mongo.serial.MapSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ReflexConfig extends Configuration {

    public ReflexConfig(Plugin plugin) {
        super(plugin);
        load();

        for(CheckType checkType : CheckType.values()) {
            if(!inspectPeriods.containsKey(checkType.getName())) {
                inspectPeriods.put(checkType.getName(), 60);
            }
        }

        save();
    }

    @ConfigData("enabled") private boolean enabled = true;
    @ConfigData("alert-format-detail") private String alertFormatDetail = "&7[&c&lAres&r&7] %player&7(&c%totalvlxVL&7)&r &7failed &9%check" +
            "&7(%detail&7)[&c%vlVL&7]";
    @ConfigData("alert-format") private String alertFormat = "&7[&c&lAres&r&7] %player&7(&c%totalvlxVL&7)&r &7failed &9%check" +
            "&7[&c%vlVL&7]";

    @ConfigData("prefix") private String prefix = "&7[&c&lAres&r&7]";

    @ConfigData("check.checkdata.passlevel.autoclick")
    private double passLevelAutoClick = 0.80;

    @ConfigData("inspect.train.create-neurons")
    private int trainingCreateNeurons = 10;

    @ConfigData("inspect.periods")
    @ConfigSerializer(serializer = MapSerializer.class)
    private Map<String, Integer> inspectPeriods = new HashMap<>(); //seconds

    public int getInspectPeriod(CheckType checkType) {
        return inspectPeriods.get(checkType.getName());
    }

}
