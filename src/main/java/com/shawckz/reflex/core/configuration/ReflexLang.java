/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.core.configuration;

public enum ReflexLang {

    ALERTS_ENABLED("&7Alerts have been &aenabled&7."),
    ALERTS_DISABLED("&7Alerts have been &cdisabled&7."),
    ALERT_PREFIX("&7[&c!&7]"),
    ALERT_PREVENT("&9{0}&7 failed &c{1} [&5{2}VL&7]"),
    ALERT_PREVENT_DETAIL("&9{0}&7 failed &c{1} &7(&a{2}&7) [&5{3}VL&7]"),
    INSPECT_START("&7Starting inspection on &9{0}&7 for &a{1}s &7[&c{2}&7]"),
    INSPECT_FINISH("&7Finished inspection on &9{0} &7(&6{1}&7) &7[&c{2}&7]"),
    TRAIN_START("&7Starting training on &9{0}&7 for &a{1}s&7 [&c{2}&7]"),
    TRAIN_FINISH("&7Finished training on &9{0}&7 [&c{1}&7]"),
    ;

    private final String defaultValue;

    ReflexLang(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static ReflexLang fromString(String s){
        for(ReflexLang lang : values()){
            if(lang.toString().equalsIgnoreCase(s)){
                return lang;
            }
        }
        return null;
    }

}
