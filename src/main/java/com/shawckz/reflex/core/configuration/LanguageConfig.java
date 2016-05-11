/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.core.configuration;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LanguageConfig extends Configuration {

    public LanguageConfig(Plugin plugin) {
        super(plugin, "lang.yml");
        load();
        save();

        for(ReflexLang lang : ReflexLang.values()){//Load default values into local memory hashmap cache
            if (!this.lang.containsKey(lang)) {
                this.lang.put(lang, lang.getDefaultValue());
            }
        }

        for(ReflexLang key : lang.keySet()){//Save values into config that don't exist already
            if(!getConfig().contains(key.toString())){
                getConfig().set(key.toString(), lang.get(key));
            }
        }

        for(String key : getConfig().getKeys(false)){//Load lang values from config into local memory hashmap cache
            ReflexLang lang = ReflexLang.fromString(key);
            if(lang != null){
                this.lang.put(lang, getConfig().getString(key));
            }
        }
        save();
    }


    private final Map<ReflexLang, String> lang = new HashMap<>();

    public String getFormattedLang(ReflexLang lang, String... args) {
        if (!this.lang.containsKey(lang)) {
            this.lang.put(lang, lang.getDefaultValue());
        }
        String val = this.lang.get(lang);
        if (args != null) {
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if(val.contains("{"+i+"}")){
                        val = val.replace("{" + i + "}", args[i]);
                    }
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', val);
    }

}
