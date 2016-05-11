/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.bridge;

import org.apache.commons.lang.StringUtils;

/**
 * The CheckType enumerator
 * Used to finally represent a check's hack that is being prevented
 * or detected
 */
public enum CheckType {

    FLY(),
    SPEED,
    VCLIP("VClip", true),
    AUTO_CLICK("AutoClick", true),
    TRIGGER_BOT("TriggerBot"),
    AURA(true),
    ANTI_KNOCKBACK("AntiKnockback"),
    FAST_BOW("FastBow", true),
    CRITICALS,
    REGEN(true),
    NO_SLOW_DOWN("NoSlowDown"),
    AURA_TWITCH("AuraTwitch", true),
    PHASE,
    BAD_PACKETS("BadPackets"),
    BED_FLY("BedFly"),
    ANGLE,
    REACH,
    NO_SWING,
    HEAD_ROLL("HeadRoll"),
    TAB_COMPLETE("TabComplete"),
    HIGH_JUMP("HighJump");

    private final String name;
    private final boolean trigger;
    CheckType(String name){
        this.name = name;
        this.trigger = false;
    }

    CheckType(String name, boolean trigger) {
        this.name = name;
        this.trigger = trigger;
    }

    CheckType(boolean trigger) {
        this.name = StringUtils.capitalize(name().toLowerCase().replaceAll("_", ""));
        this.trigger = trigger;
    }

    CheckType() {
        this.name = StringUtils.capitalize(name().toLowerCase().replaceAll("_", ""));
        this.trigger = false;
    }

    public String getName() {
        return name;
    }

    public boolean isTrigger() {
        return trigger;
    }

    @SuppressWarnings("all")
    @Override
    public String toString() {
        return getName();
    }
}