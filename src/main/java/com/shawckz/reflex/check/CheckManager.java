package com.shawckz.reflex.check;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.checks.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

public class CheckManager {

    @Getter private final Map<CheckType,Check> checks;
    private static CheckManager instance;

    protected CheckManager(){
        checks = new HashMap<>();
    }

    public void setupChecks(){
        checks.put(CheckType.SPEED, new CheckSpeed());
        checks.put(CheckType.HEAD_ROLL, new CheckHeadRoll());
        checks.put(CheckType.NO_SWING, new CheckNoSwing());
        checks.put(CheckType.NO_SLOW_DOWN, new CheckNoSlowdown());
        checks.put(CheckType.BED_FLY, new CheckBedFly());
        checks.put(CheckType.TAB_COMPLETE, new CheckTabComplete());
        checks.put(CheckType.VCLIP, new CheckVClip());
        checks.put(CheckType.FAST_BOW, new CheckFastBow());
        checks.put(CheckType.REGEN, new CheckRegen());
        checks.put(CheckType.AUTO_CLICK, new CheckAutoClick());
        checks.put(CheckType.REACH, new CheckReach());
        checks.put(CheckType.PHASE, new CheckPhase());

        for(Check c : checks.values()){
            c.setup();
            c.saveConfig();
            if(c.isEnabled()){
                Bukkit.getPluginManager().registerEvents(c, Reflex.getPlugin());
            }
        }
    }

    public static CheckManager get() {
        if (instance == null) {
            synchronized (CheckManager.class) {
                if (instance == null) {
                    instance = new CheckManager();
                }
            }
        }
        return instance;
    }

    public final boolean hasCheck(String name){
        return getCheck(name) != null;
    }

    public final Check getCheck(String name){
        return getCheck(CheckType.valueOf(name));
    }

    public final boolean hasCheck(CheckType hack){
        return getCheck(hack) != null;
    }

    public final Check getCheck(CheckType hack){
        if (checks.containsKey(hack)) {
            return checks.get(hack);
        }
        return null;
    }

}
