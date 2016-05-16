/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.base;

import com.shawckz.reflex.check.data.CheckData;
import com.shawckz.reflex.check.data.checkdata.DataAutoClick;
import com.shawckz.reflex.check.data.checkdata.DataVClip;
import org.apache.commons.lang.StringUtils;

/**
 * The CheckType enumerator
 * Used to finally represent a oldcheck's hack that is being prevented
 * or detected
 */
public enum CheckType {

    FLY(null),
    SPEED(null),
    VCLIP("VClip", true, DataVClip.class),
    AUTO_CLICK("AutoClick", true, DataAutoClick.class),
    TRIGGER_BOT("TriggerBot", null),
    AURA(false, null),
    ANTI_KNOCKBACK("AntiKnockback", null),
    FAST_BOW("FastBow", false, null),
    CRITICALS(null),
    REGEN(false, null),
    NO_SLOW_DOWN("NoSlowDown", null),
    AURA_TWITCH("AuraTwitch", false, null),
    PHASE(null),
    BAD_PACKETS("BadPackets", null),
    BED_FLY("BedFly", null),
    ANGLE(null),
    REACH(null),
    NO_SWING(null),
    HEAD_ROLL("HeadRoll", null),
    TAB_COMPLETE("TabComplete", null),
    HIGH_JUMP("HighJump", null);

    private final String name;
    private final boolean capture;
    private final Class<? extends CheckData> data;

    CheckType(String name, Class<? extends CheckData> data) {
        this.name = name;
        this.capture = false;
        this.data = data;
    }

    CheckType(String name, boolean capture, Class<? extends CheckData> data) {
        this.name = name;
        this.capture = capture;
        this.data = data;
    }

    CheckType(boolean capture, Class<? extends CheckData> data) {
        this.name = StringUtils.capitalize(name().toLowerCase().replaceAll("_", ""));
        this.capture = capture;
        this.data = data;
    }

    CheckType(Class<? extends CheckData> data) {
        this.name = StringUtils.capitalize(name().toLowerCase().replaceAll("_", ""));
        this.capture = false;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public boolean isCapture() {
        return capture;
    }

    public Class<? extends CheckData> getData() {
        return data;
    }

    public static CheckType fromString(String s) {
        for(CheckType checkType : values()) {
            if(checkType.getName().equalsIgnoreCase(s) || checkType.toString().equalsIgnoreCase(s)) {
                return checkType;
            }
        }
        return null;
    }

    @SuppressWarnings("all")
    @Override
    public String toString() {
        return getName();
    }
}