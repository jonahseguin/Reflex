/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data;


import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.util.obj.TrigUtils;
import com.jonahseguin.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Getter
@Setter
@NoArgsConstructor
@CollectionName(name = "reflex_checkdata")
public class PlayerData extends CheckData {

    @Transient
    public static final Set<Material> SOLID_MATERIAL_WHITELIST = new HashSet<>();

    @Transient
    public static final Set<Integer> SPECIAL_SOLID_MATERIAL_ID_WHITELIST = new HashSet<>(); //Fences, etc

    static {
        SOLID_MATERIAL_WHITELIST.addAll(Arrays.asList(Material.SNOW, Material.SNOW_BLOCK, Material.CARPET, Material.DIODE,
                Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF,
                Material.REDSTONE_COMPARATOR_ON, Material.SKULL, Material.SKULL_ITEM, Material.LADDER, Material.WATER_LILY));

        SPECIAL_SOLID_MATERIAL_ID_WHITELIST.addAll(Arrays.asList(85, 188, 189, 190, 191, 192, 113, 107, 183, 184, 185, 186, 187, 139, 65));
    }

    public Location from = null;

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

    public long lastGroundTimeUpdate = -1;
    public long lastGroundTime = -1;
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

    public boolean onGround = false;
    public boolean onLadder = false; //Also vines
    public boolean inWater = false;
    public boolean inLava = false;
    public boolean onIce = false;
    public boolean underBlock = false;
    public boolean onPiston = false;
    public boolean inWeb = false;

    public Location setBack = null;

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

    public void updateMoveValues(Location to) {
        //ground, ladder, liquid, ice, under, inside, piston
        onGround = (onLadder = inWater = inLava = onIce = underBlock = onPiston = inWeb = false);

        for (Location loc : getLocationsAround(to)) {
            Material below = loc.clone().add(0, -1.0E-13D, 0).getBlock().getType();
            Material liquid = loc.clone().add(0, 0.0625D, 0).getBlock().getType();
            Material special = loc.clone().add(0, -0.5000000000001D, 0).getBlock().getType();
            Material above = loc.clone().add(0, 2.2D, 0).getBlock().getType();
            if (!onGround) {
                onGround = isSolid(below);
            }
            if (!inWeb) {
                inWeb = below.equals(Material.WEB) || above.equals(Material.WEB);
            }
            if (!onLadder) {
                onLadder = below.equals(Material.LADDER) || above.equals(Material.LADDER) || below.equals(Material.VINE) || above.equals(Material.VINE);
            }
            if (!inWater) {
                inWater = liquid.equals(Material.STATIONARY_WATER) || liquid.equals(Material.WATER) || above.equals(Material.STATIONARY_WATER) || above.equals(Material.WATER);
            }
            if (!inLava) {
                inLava = liquid.equals(Material.STATIONARY_LAVA) || liquid.equals(Material.LAVA) || above.equals(Material.STATIONARY_LAVA) || above.equals(Material.LAVA);
            }
            if (!onPiston) {
                onPiston = below.equals(Material.PISTON_BASE) || below.equals(Material.PISTON_EXTENSION) || below.equals(Material.PISTON_MOVING_PIECE) || below.equals(Material.PISTON_STICKY_BASE);
            }
            if (!onIce) {
                onIce = below.equals(Material.ICE) || below.equals(Material.PACKED_ICE);
            }
            if (!underBlock) {
                underBlock = isSolid(above);
            }
        }

        if (to.getBlock().getType().isTransparent()) {
            setBack = to;
        }

    }

    public boolean isSolid(Material material) {
        if (material.isSolid() || SOLID_MATERIAL_WHITELIST.contains(material)) {
            return true;
        }
        else if (SPECIAL_SOLID_MATERIAL_ID_WHITELIST.contains(material.getId())) {
            return true;
        }
        return false;
    }

    public boolean isOnGround(Location location) {
        for (Location loc : getLocationsAround(location)) {
            Material below = loc.clone().add(0, -1.0E-13D, 0).getBlock().getType();
            if (below.isSolid() || SOLID_MATERIAL_WHITELIST.contains(below)) {
                return true;
            }
            //For when standing on fences, etc.
            Material specialBelow = loc.clone().add(0, -0.5000000000001D, 0).getBlock().getType();
            if (SPECIAL_SOLID_MATERIAL_ID_WHITELIST.contains(specialBelow.getId())) {
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
                if (!lf.isAnnotationPresent(Transient.class)) {
                    lf.setAccessible(true);
                    lf.set(this, data.get(f));
                }
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
                if (!f.isAnnotationPresent(Transient.class)) {
                    f.setAccessible(true);
                    data.put(f, f.get(this));
                }
            }
        }
        catch (IllegalAccessException ex) {
            throw new ReflexException("Could not fetch data from checkdata", ex);
        }

        return data;
    }

}
