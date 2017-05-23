/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jonahseguin.reflex.check.checks.combat.CheckReach;
import com.jonahseguin.reflex.util.obj.TrigUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import javax.persistence.Transient;
import java.util.*;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 17:10.
 * Project: Reflex
 */
@Getter
@Setter
public class PlayerData {

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

    public final Player player;

    /* Jesus */
    public long jesusTime = 0;

    /* GodMode */
    public int keepAlivePackets = 0;
    public long lastKeepAlivePacket = 0;

    /* FastEat */
    public boolean eatDidInteract = false;
    public long eatInteract = 0;
    public long eatConsume = 0;
    public Material eatMaterial = null;
    /* Reach */
    public Set<CheckReach.ReachLog> reaches = Sets.newHashSet();
    public long lastReach = 0;
    /* NoSwing */
    public boolean hasSwung = false;
    public long lastSwing = 0;
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
    public Location from = null;

    public PlayerData(Player player) {
        this.player = player;
    }

    public Set<Double> getReachDistancesAsDoubles() {
        Set<Double> doubles = new HashSet<>();
        for (CheckReach.ReachLog log : reaches) {
            doubles.add(log.getDistance());
        }
        return doubles;
    }

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

    public List<Player> getNearby(int r1, int r2, int r3) {
        List<Entity> entities = player.getNearbyEntities(r1, r2, r3);
        List<Player> players = Lists.newArrayList();
        entities.forEach(en -> {
            if (en instanceof Player) {
                players.add(((Player) en));
            }
        });
        return players;
    }

    public double degreeTo180(double degree) {
        if ((degree %= 360.0) >= 180.0) {
            degree -= 360.0;
        }
        if (degree < -180.0) {
            degree += 360.0;
        }
        return degree;
    }

    public Vector getRelativeRotation(Location axis, Location relative) {
        double dx = relative.getX() - axis.getX();
        double dy = relative.getY() - axis.getY();
        double dz = relative.getZ() - axis.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793);
        return new Vector(yaw, pitch, 0.0f);
    }

    // Yaw, Pitch
    public Map.Entry<Double, Double> getTargetOffset(LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = getRelativeRotation(playerLoc, entityLoc);
        double deltaYaw = degreeTo180(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = degreeTo180(playerRotation.getY() - expectedRotation.getY());
        return new AbstractMap.SimpleEntry<>(deltaYaw, deltaPitch);
    }

    private double fixX(double x) {
        double touchedX = x;
        double rem = touchedX - (double) Math.round(touchedX) + 0.01;
        if (rem < 0.3) {
            touchedX = NumberConversions.floor(x) - 1;
        }
        return touchedX;
    }

    public boolean isFullyInWater(Location loc) {
        double touchedX = fixX(loc.getX());
        return new Location(loc.getWorld(), touchedX, loc.getY(), loc.getBlockZ()).getBlock().isLiquid()
                && new Location(loc.getWorld(), touchedX, Math.round(loc.getY()), loc.getBlockZ()).getBlock().isLiquid();
    }

    public boolean isFullyInWater() {
        return isFullyInWater(player.getLocation());
    }

    public boolean isAboveWater(int blocks, Location loc) {
        for (int x = loc.getBlockY(); x > loc.getBlockX() - blocks; x--) {
            Block newLoc = new Location(loc.getWorld(), loc.getBlockX(), x, loc.getBlockZ()).getBlock();
            if (newLoc.getType() != Material.AIR) {
                return newLoc.isLiquid();
            }
        }
        return false;
    }

    public boolean isAboveWater() {
        return isAboveWater(25, player.getLocation());
    }

    public boolean cannotStandWater(Block block) {
        Block otherBlock = block.getRelative(BlockFace.DOWN);
        boolean isHover = block.getType() == Material.AIR;
        boolean n = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean s = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER;
        boolean e = otherBlock.getRelative(BlockFace.EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER;
        boolean w = otherBlock.getRelative(BlockFace.WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER;
        boolean ne = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER;
        boolean nw = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER;
        boolean se = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean sw = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER;
        return n && s && e && w && ne && nw && se && sw && isHover;
    }

}
