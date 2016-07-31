/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.trigger.simple.CheckXray;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public class XrayStats {

    private Map<Stat, Double> stats = new HashMap<>();


    public XrayStats() {
        for (Stat stat : Stat.values()) {
            stats.put(stat, 0D);
        }
    }

    public boolean isTracking(Material type) {
        for (Stat stat : Stat.values()) {
            if (stat.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public Stat convertStat(Material type) {
        for (Stat stat : Stat.values()) {
            if (stat.type.equals(type)) {
                return stat;
            }
        }
        return null;
    }

    public Map<Stat, Double> getStats() {
        return stats;
    }

    public double getStat(Stat stat) {
        return stats.get(stat);
    }

    public double modifyStat(Stat stat, double change) {
        double current = 0;
        if (stats.containsKey(stat)) {
            current = stats.get(stat);
        }
        current += change;
        stats.put(stat, current);
        return current;
    }

    public double getMax(Stat stat) {
        CheckXray checkXray = (CheckXray) Reflex.getInstance().getTriggerManager().getTrigger(CheckType.XRAY);
        if (checkXray.getMax().containsKey(stat)) {
            return checkXray.getMax().get(stat);
        }
        else {
            return stat.max;
        }
    }

    public boolean overMax(Stat stat) {
        if (stats.containsKey(stat)) {
            if (stats.get(stat) >= getMax(stat)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        stats.clear();
        for (Stat stat : Stat.values()) {
            stats.put(stat, 0D);
        }
    }

    public enum Stat {
        DIAMOND(32D, Material.DIAMOND_ORE),
        EMERALD(40D, Material.EMERALD_ORE),
        GOLD(45D, Material.GOLD_ORE);

        public final double max;
        public final Material type;

        Stat(double max, Material type) {
            this.max = max;
            this.type = type;
        }
    }

}
