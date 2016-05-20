/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.backend.configuration;

public enum ReflexLang {

    ALERTS_ENABLED("&7[&c!&7] &7Alerts have been &aenabled&7."),
    ALERTS_DISABLED("&7[&c!&7] &7Alerts have been &cdisabled&7."),
    ALERT_PREFIX("&7[&c!&7] "),
    ALERT_TRIGGER("&7Inspection triggered on &9{0}&7 for &c{1}&7."),
    ALERT_INSPECT_MANUAL("&a{0}&7 is inspecting &9{1}&7 for &c{2}&7 (&c{3}s&7)"),
    INSPECT_MANUAL_RESULT("&7Inspection result for &9{0}&7: &6{1}"),
    ALERT_INSPECT("&9{0}&7 failed inspection for &c{1} &7[&c{2}VL&7]"),
    ALERT_FAIL("&9{0} &4failed &c{1} &7[&c{2}&7VL]"),
    ALERT_FAIL_DETAIL("&9{0} &4failed &c{1} &7({2}&7) &7[&c{3}&7VL]"),
    ALERT_INSPECT_DETAIL("&9{0}&7 &4failed&7 inspection &c{1} &7({2}&7) [&c{3}VL&7]"),
    ALERT_INSPECT_PASS("&9{0}&7 &2passed&7 inspection &c{1}"),
    ALERT_INSPECT_PASS_DETAIL("&9{0}&7 &2passed&7 inspection &c{1} &7({2}&7)"),
    AUTOBAN("&9{0}&7 will be&c banned&7 for &6{1}&7 in {2}s.  &2&l[CANCEL]"),
    AUTOBAN_BANNED("&9{0}&7 has been&c banned&7 for &6{1}&7. &2&l[REVERT]"),
    CANCEL_NOT_BEING_BANNED("&cThat player is not being auto-banned."),
    CANCEL("&9{0}&7 is not longer being banned."),
    PLAYER_NOT_FOUND("&cPlayer '{0}' not found."),
    CHECK_NOT_FOUND("&cCheck '{0}' not found."),
    HEADER_FOOTER("&7&m------------------------");

    private final String defaultValue;

    ReflexLang(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static ReflexLang fromString(String s) {
        for (ReflexLang lang : values()) {
            if (lang.toString().equalsIgnoreCase(s)) {
                return lang;
            }
        }
        return null;
    }

}
