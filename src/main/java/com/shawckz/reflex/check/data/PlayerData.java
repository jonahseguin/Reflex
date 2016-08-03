/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data;


import com.shawckz.reflex.backend.database.mongo.annotations.CollectionName;
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


    public double aimValue = 0;
    public double aimYawValue = 0;
    public double aimOffset = 0;

    public double[] clicksPerSecond = {0, 0, 0, 0};

    public double blocksPerSecond = 0;
    public double hFreedom = 0;
    public int bhopDelay = 0;

    public boolean hasSwung = false;

    public boolean enteredBed = false;
    public long bowPull = 0;
    public long bowShoot = 0;
    public double bowPower = 0;
    public double healthPerSecond = 0;

    public boolean jumping = false;

    public boolean triedVClip = false;
    public int vclipY = -1;
    public Location lastVClipLocation = null;

    public XrayStats xrayStats = new XrayStats();

    public long lastGroundtime = -1;
    public boolean hasPositiveVelocity = false;
    public double fallingVelocity = -1D;
    public double fallingVelocityY = -1D;
    public double lastFallingVelocity = -1D;
    public double lastFallingVelocityY = -1D;

    public boolean triedPhase = false;
    public int phaseY = -1;
    public Location lastPhaseLocation = null;
    public long lastPhaseTime = -1;
    public int linkedPhaseAttempts = 0;

    public int hits = 0;
    public int misses = 0;
    public long lastHitTime = -1;
    public long lastMissTime = -1;
    public int consecutiveHits = 0;
    public int consecutiveMisses = 0;

    public int packets = 0;

    public Player target = null;
    public Player lastTarget = null;

    public float lastYaw = -1;

    public double consecutiveCriticalHits = 0;
    public double lastCriticalY = -1;
    public double totalCriticalY = 0;

    public float aimSpeed = 0F;
    public float lastAimSpeed = 0F;

    public long trigLastCheck = -1L;
    public double cpsOn = 0;
    public double cpsOff = 0;

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

    public void load(Map<Field, Object> data) {
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
