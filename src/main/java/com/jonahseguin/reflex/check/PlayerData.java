/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.oldchecks.data.XrayStats;
import com.jonahseguin.reflex.util.obj.TrigUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.persistence.Transient;
import java.util.*;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 17:10.
 * Project: Reflex
 */
@Getter
@Setter
public class PlayerData {

    public final Player player;

    public PlayerData(Player player) {
        this.player = player;
    }



    @Transient
    private static final Set<Material> SOLID_MATERIAL_WHITELIST = new HashSet<>();

    @Transient
    private static final Set<Integer> SPECIAL_SOLID_MATERIAL_ID_WHITELIST = new HashSet<>(); //Fences, etc

    static {
        SOLID_MATERIAL_WHITELIST.addAll(Arrays.asList(Material.SNOW, Material.SNOW_BLOCK, Material.CARPET, Material.DIODE,
                Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF,
                Material.REDSTONE_COMPARATOR_ON, Material.SKULL, Material.SKULL_ITEM, Material.LADDER, Material.WATER_LILY));

        SPECIAL_SOLID_MATERIAL_ID_WHITELIST.addAll(Arrays.asList(85, 188, 189, 190, 191, 192, 113, 107, 183, 184, 185, 186, 187, 139, 65));
    }

    /* NoFall */
    public double fallDistance = 0;


    /* Speed */
    public double blocksPerSecond = 0;
    public double hFreedom = 0;
    public int bhopDelay = 0;

    /* FastBow */
    public long bowPull = 0;
    public long bowShoot = 0;
    public double bowPower = 0;

    /* Regen */
    public double healthPerSecond = 0;

    /* VClip */
    public boolean triedVClip = false;
    public int vclipY = -1;
    public Location lastVClipLocation = null;

    /* Xray */
    public XrayStats xrayStats = new XrayStats();

    /* Phase */
    public boolean triedPhase = false;
    public int phaseY = -1;
    public Location lastPhaseLocation = null;
    public long lastPhaseTime = -1;
    public int linkedPhaseAttempts = 0;

    /* MorePackets */
    public int packets = 0;

    /* Player world variables */
    public boolean onGround = false;
    public boolean onLadder = false; //Also vines
    public boolean inWater = false;
    public boolean inLava = false;
    public boolean onIce = false;
    public boolean underBlock = false;
    public boolean onPiston = false;
    public boolean inWeb = false;

    // General setBack --> Last safe location
    public Location setBack = null;

    public boolean isInLiquid() {
        Material m = player.getLocation().getBlock().getType();
        return m == Material.WATER || m == Material.STATIONARY_WATER
                || m == Material.LAVA || m == Material.STATIONARY_LAVA;
    }

    public boolean isOnGround() {
        return isOnGround(player.getLocation());
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
        } else if (SPECIAL_SOLID_MATERIAL_ID_WHITELIST.contains(material.getId())) {
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

    public List<Block> getBlocksAround(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (double x = -radius; x <= radius; x ++) {
            for (double y = -radius; y <= radius; y++)
            for (double z = -radius; z <= radius; z++) {
                blocks.add(location.clone().add(x, 0, z).getBlock());
            }
        }
        blocks.add(location.getBlock());

        return blocks;
    }

}
