/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data;


import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.backend.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.util.obj.TrigUtils;
import com.shawckz.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Getter
@Setter
@NoArgsConstructor
@CollectionName(name = "reflex_checkdata")
public class PlayerData extends CheckData {

    /**
     * AutoClick
     */
    @MongoColumn(name = "clicksPerSecond")
    private double[] clicksPerSecond = {0, 0, 0, 0};

    /**
     * Speed
     */
    @MongoColumn(name = "blocksPerSecond")
    private double blocksPerSecond = 0;

    private double hFreedom = 0;

    private int bhopDelay = 0;

    /**
     * NoSwing
     */
    @MongoColumn(name = "hasSwung")
    private boolean hasSwung = false;

    /**
     * BedFly
     */
    @MongoColumn(name = "enteredBed")
    private boolean enteredBed = false;

    /**
     * FastBow
     */
    @MongoColumn(name = "bowPull")
    private long bowPull = 0;
    @MongoColumn(name = "bowShoot")
    private long bowShoot = 0;
    @MongoColumn(name = "bowPower")
    private double bowPower = 0;

    /**
     * Regen
     */
    @MongoColumn(name = "healthPerSecond")
    private double healthPerSecond = 0;

    /**
     * HighJump
     */
    @MongoColumn(name = "jumping")
    private boolean jumping = false;

    /**
     * VClip
     */

    private boolean triedVClip = false;
    private int vclipY = -1;
    private Location lastVClipLocation = null;

    /**
     * Xray
     */
    private XrayStats xrayStats = new XrayStats();

    /**
     * Fly
     */
    private long lastGroundtime = -1;
    private boolean hasPositiveVelocity = false;
    private double fallingVelocity = -1D;
    private double fallingVelocityY = -1D;
    private double lastFallingVelocity = -1D;
    private double lastFallingVelocityY = -1D;
    /**
     * Phase
     */
    private boolean triedPhase = false;
    private int phaseY = -1;
    private Location lastPhaseLocation = null;
    private long lastPhaseTime = -1;
    private int linkedPhaseAttempts = 0;
    /**
     * Accuracy
     */
    private int hits = 0;
    private int misses = 0;
    private long lastHitTime = -1;
    private long lastMissTime = -1;
    private int consecutiveHits = 0;
    private int consecutiveMisses = 0;
    /**
     * MorePackets
     */
    private int packets = 0;
    /**
     * Aura
     */
    private Player target = null;
    private Player lastTarget = null;
    /**
     * Aura Twitch
     */
    private float lastYaw = -1;
    /**
     * Criticals
     */
    private double consecutiveCriticalHits = 0;
    private double lastCriticalY = -1;
    private double totalCriticalY = 0;
    /**
     * Smooth Aim
     */
    private float aimSpeed = 0F;
    private float lastAimSpeed = 0F;
    /**
     * Triggerbot
     */
    private long trigLastCheck = -1L;
    private double cpsOn = 0;
    private double cpsOff = 0;

    public void updateFallingVelocity(double x, double y, double z) {
        this.lastFallingVelocity = this.fallingVelocity;
        this.lastFallingVelocityY = this.fallingVelocityY;

        this.fallingVelocity = x + z;
        this.fallingVelocityY = y;


        if (this.lastFallingVelocityY == -1D) {
            this.lastFallingVelocityY = this.fallingVelocityY;
        }
        if (this.lastFallingVelocity == -1D) {
            this.lastFallingVelocity = this.fallingVelocity;
        }
    }

    /**
     * Util methods
     */

    public boolean underBlock(Location location) {
        for (Location loc : getLocationsAround(location)) {
            // Material below = loc.clone().add(0, -1.0D, 0).getBlock().getType();
            Material above = loc.clone().add(0, 2.2D, 0).getBlock().getType();
            if (above.isSolid()) {
                return true;
            }
        }
        return false;
    }


    public boolean onType(Location location, Material type) {
        for (Location loc : getLocationsAround(location)) {
            Material below = loc.clone().add(0, -1.0D, 0).getBlock().getType();
            // Material above = loc.clone().add(0, 2.2D, 0).getBlock().getType();
            if (below.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean underType(Location location, Material type) {
        for (Location loc : getLocationsAround(location)) {
            //Material below = loc.clone().add(0, -1.0D, 0).getBlock().getType();
            Material above = loc.clone().add(0, 2.2D, 0).getBlock().getType();
            if (above.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public double getHDistance(Location from, Location to) {
        return TrigUtils.getDistance(to.getX(), to.getZ(), from.getX(), from.getZ());
    }

    public Block getBlockUnderPlayer(Player player) {
        return player.getLocation().getBlock().getLocation().clone().subtract(0, 1, 0).getBlock();
    }

    public boolean isOnGround(Location location) {
        for (Location loc : getLocationsAround(location)) {
            Material below = loc.clone().add(0, -1.0D, 0).getBlock().getType();
            //Material above = loc.clone().add(0, 2.2D, 0).getBlock().getType();
            if (below.isSolid()) {
                return true;
            }
        }
        return false;
    }

    //Including the location you pass
    public List<Location> getLocationsAround(Location location) {
        List<Location> locations = new ArrayList<>();
        for (double x = -0.3D; x <= 0.3D; x += 0.3D) {
            for (double z = -0.3D; z <= 0.3D; z += 0.3D) {
                locations.add(location.clone().add(x, 0, z));
            }
        }

        locations.add(location);

        return locations;
    }

    /**
     * Clone this object
     */
    public PlayerData copy() {
        PlayerData playerData = new PlayerData();
        playerData.load(getData());
        return playerData;
    }

    private void load(Map<Field, Object> data) {
        try {
            for (Field f : data.keySet()) {
                Field lf = this.getClass().getDeclaredField(f.getName());
                lf.setAccessible(true);
                lf.set(this, data.get(f));
            }
        }
        catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new ReflexException("Could not load data into checkdata", ex);
        }
    }

    public Map<Field, Object> getData() {
        Map<Field, Object> data = new HashMap<>();

        try {
            for (Field f : this.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                data.put(f, f.get(this));
            }
        }
        catch (IllegalAccessException ex) {
            throw new ReflexException("Could not fetch data from checkdata", ex);
        }

        return data;
    }

}
