package com.shawckz.reflex.check;

import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.check.checker.checkers.CheckerAutoClick;
import org.apache.commons.lang.StringUtils;

/**
 * The CheckType enumerator
 * Used to finally represent a check's hack that is being prevented
 * or detected
 */
public enum CheckType {

    FLY(),
    SPEED,
    VCLIP("VClip"),
    AUTO_CLICK("AutoClick", CheckerAutoClick.class),
    TRIGGER_BOT("TriggerBot"),
    AURA,
    ANTI_KNOCKBACK("AntiKnockback"),
    FAST_BOW("FastBow"),
    CRITICALS,
    REGEN,
    NO_SLOW_DOWN("NoSlowDown"),
    AURA_TWITCH("AuraTwitch"),
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
    private final Class<? extends Checker> checker;

    CheckType(String name, Class<? extends Checker> checker){
        this.name = name;
        this.checker = checker;
    }
    CheckType(Class<? extends Checker> checker) {
        this.name = StringUtils.capitalize(name().toLowerCase().replaceAll("_", ""));
        this.checker = checker;
    }

    public Class<? extends Checker> getChecker() {
        return checker;
    }

    public String getName() {
        return name;
    }
    @SuppressWarnings("all")
    @Override
    public String toString() {
        return getName();
    }
}