/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import org.apache.commons.lang.WordUtils;

/**
 * The CheckType enumerator
 * Used to finally represent a Check's hack that is being prevented
 * or detected
 */
public enum CheckType {

    VCLIP("VClip"), // DONE
    AUTO_CLICK("AutoClick"), //
    REGEN("Regen"), // DONE
    HEAD_ROLL("HeadRoll"), // DONE
    TAB_COMPLETE("TabComplete"), // DONE
    FAST_BOW("FastBow"), // DONE
    SPEED("Speed"), // DONE
    REACH("Reach"), // DONE
    WRONG_TARGET("WrongTarget"), //
    XRAY("Xray"), //
    FLY("Fly"), //
    AURA_TWITCH("AuraTwitch"), //
    ACCURACY("Accuracy"), //
    MORE_PACKETS("MorePackets"), // DONE
    PHASE("Phase"), // DONE
    CRITICALS("Criticals"), //
    SMOOTH_AIM("SmoothAim"), //
    HEALTH_TAGS("HealthTags"), //
    TRIGGER_BOT("TriggerBot"), //
    ANTI_KNOCKBACK("AntiKnockback"), // DONE
    NO_SLOW_DOWN("NoSlowDown"), //TODO
    BAD_PACKETS("BadPackets"), //TODO
    BED_FLY("BedFly"), // DONE
    NO_SWING("NoSwing"), // DONE
    HIGH_JUMP("HighJump"), //
    GLIDE("Glide"), //TODO
    FAST_EAT("FastEat"), // DONE
    SELF_HIT("SelfHit"), // DONE
    AUTO_SOUP("AutoSoup"), //
    BLINK("Blink"), //
    ATTACK_SPEED("AttackSpeed"), //
    GOD_MODE("GodMode"), // DONE
    BLOCK_HIT("BlockHit"), // DONE
    NO_FALL("NoFall"), // DONE
    SNEAK("Sneak"), // DONE
    JESUS("Jesus"); //

    private final String name;

    CheckType() {
        this.name = WordUtils.capitalizeFully(super.toString().replaceAll("_", " ")).replaceAll(" ", "");
    }

    CheckType(String name) {
        this.name = name;
    }

    public static CheckType fromString(String s) {
        for (CheckType checkType : values()) {
            if (checkType.getName().equalsIgnoreCase(s) || checkType.toString().equalsIgnoreCase(s)) {
                return checkType;
            }
        }
        return null;
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