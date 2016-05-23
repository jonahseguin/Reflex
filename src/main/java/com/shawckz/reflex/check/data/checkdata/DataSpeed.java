/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.checkdata;

import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.check.data.CheckData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

@Getter
@Setter
@CollectionName(name = "reflex_checkdata")
public class DataSpeed extends CheckData {

    private Set<SpeedData> data = new HashSet<>();

    private Set<SpeedData> bpsData = new HashSet<>();

    private double peakBlocksPerSecond = 0;

    private double blocksPerSecond = 0;

    @Getter
    public static class SpeedData {

        private double ping;
        private List<PotionEffect> potionEffects = new ArrayList<>();
        private double distance;

        public SpeedData(Player p, double distance) {
            this.ping = ((CraftPlayer) p).getHandle().ping;
            this.potionEffects.addAll(p.getActivePotionEffects());
            this.distance = distance;
        }
    }

}
