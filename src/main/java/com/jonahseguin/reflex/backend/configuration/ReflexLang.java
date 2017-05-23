/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.backend.configuration;

public enum ReflexLang {

    PLAYER_ONLY_COMMAND("&cThis is a player-only command."),
    ALERTS_ENABLED("&7[&c!&7] &7Alerts have been &aenabled&7."),
    ALERTS_DISABLED("&7[&c!&7] &7Alerts have been &cdisabled&7."),
    ALERT_PREFIX("&7[&c!&7] "),
    ALERT_SINGLE("&9{0} &7failed &c{1} &7({2}ms&7) &7({3}&7) &7[{4}&7VL]"), // player failed check (50ms) (detail) [4VL]
    AUTOBAN("&9{0}&7 will be&c banned&7 for &6{1}&7 in {2}s.  &2&l[CANCEL]"),
    AUTOBAN_BANNED("&9{0}&7 has been&c banned&7 for &6{1}&7. &2&l[REVERT]"),
    CANCEL_NOT_BEING_BANNED("&cThat player is not being auto-banned."),
    CANCEL("&9{0}&7 is no longer being banned."),
    PLAYER_NOT_FOUND("&cPlayer '{0}' not found."),
    PLAYER_NOT_FOUND_DATABASE("&cPlayer '{0} not found in database.  &7(case sensitive)"),
    BAN_NOT_FOUND("&cBan with ID '{0} not found."),
    VIOLATION_NOT_FOUND("&cViolation '{0}' not found."),
    ALERT_NOT_FOUND("&cAlert by ID '{0}' not found."),
    CHECK_NOT_FOUND("&cCheck '{0}' not found."),
    HEADER_FOOTER("&7&m------------------------"),
    BANNED("&7[Reflex] \n&9You have been banned for &7[&e{0}&7]\n&7Expires &e{1}"),
    BANNED_CONFIRMED("&7[Reflex] \n&9You have been banned for &7[&e{0}&7]\n&7(Confirmed)"),
    UNBANNED("&9{0}&7 has been unbanned by &e{1}&7."),
    CONFIRM_BAN("&7The ban on &9{0}&7 has been confirmed by &e{1}&7 as &a{2}&7."),
    CONFIG_LOAD("&7The configuration was reloaded by &e{0}&7."),
    CONFIG_SET("&7Config value '{0}' set to '{1}' by &e{2}&7."),
    SETTINGS_ALL("&7Setting &e{0}&7 updated to &9{1}&7 for &aall checks &7by &e{2}&7."),
    SETTINGS("&7Setting &e{0}&7 updated to &9{1}&7 for &a{2}&7 by &e{3}&7."),
    AUTOBAN_CHEATER("&7[Reflex] &4&lYou will be automatically banned for &r{0}&4&l shortly."),
    AUTOBAN_CHEATER_CANCEL("&7[Reflex] &a&lYou are no longer being automatically banned.  &fSorry for the inconvenience.");

    private final String defaultValue;

    ReflexLang(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static ReflexLang fromString(String s) {
        for (ReflexLang lang : values()) {
            if (lang.toString().equalsIgnoreCase(s)) {
                return lang;
            }
        }
        return null;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
