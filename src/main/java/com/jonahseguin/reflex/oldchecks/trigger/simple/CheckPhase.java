/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.simple;

import com.google.common.collect.Sets;
import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Gate;

import java.util.Set;

/**
 * Created by jonahseguin on 2016-07-28.
 */
@Getter
@Setter
public class CheckPhase extends RTrigger {

    private double box = 0.42500001192093;
    private double radi = box / 2;
    @ConfigData("translucent-materials")
    private Set<String> translucentMaterialsString = Sets.newHashSet();

    private Set<Material> translucentMaterials = Sets.newHashSet();

    public CheckPhase(Reflex instance) {
        super(instance, CheckType.PHASE, RCheckType.TRIGGER);

        if (translucentMaterialsString.isEmpty()) {
            //Some high quality, beautiful code:
            Set<Material> materials = Sets.newHashSet(Material.AIR, Material.ACACIA_STAIRS, Material.ANVIL, Material.BEACON,
                    Material.BED_BLOCK, Material.BIRCH_WOOD_STAIRS, Material.BREWING_STAND,
                    Material.BRICK_STAIRS, Material.CAULDRON, Material.CHEST, Material.COBBLE_WALL, Material.COBBLESTONE_STAIRS,
                    Material.COCOA, Material.DARK_OAK_STAIRS, Material.DAYLIGHT_DETECTOR, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON,
                    Material.DRAGON_EGG, Material.ENCHANTMENT_TABLE, Material.ENDER_CHEST, Material.ENDER_PORTAL_FRAME, Material.FENCE, Material.FENCE_GATE,
                    Material.ENDER_PORTAL, Material.FLOWER_POT, Material.HOPPER, Material.IRON_FENCE, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS,
                    Material.NETHER_FENCE, Material.PISTON_BASE, Material.PISTON_EXTENSION, Material.PISTON_MOVING_PIECE, Material.PISTON_STICKY_BASE,
                    Material.QUARTZ_STAIRS, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_COMPARATOR_OFF, Material.SANDSTONE_STAIRS, Material.SKULL,
                    Material.SNOW, Material.SMOOTH_STAIRS, Material.SOUL_SAND, Material.SPRUCE_WOOD_STAIRS, Material.STEP, Material.TRAP_DOOR,
                    Material.TRAPPED_CHEST, Material.WATER_LILY, Material.WOOD_STAIRS, Material.WOOD_STEP, Material.ACTIVATOR_RAIL, Material.BROWN_MUSHROOM,
                    Material.CACTUS, Material.CARPET, Material.CARROT, Material.CROPS, Material.DEAD_BUSH, Material.DETECTOR_RAIL, Material.DOUBLE_PLANT, Material.FIRE,
                    Material.GOLD_PLATE, Material.IRON_DOOR_BLOCK, Material.IRON_PLATE, Material.LADDER, Material.LAVA, Material.LEVER, Material.LONG_GRASS,
                    Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POWERED_RAIL, Material.PUMPKIN_STEM, Material.RAILS, Material.RED_MUSHROOM,
                    Material.RED_ROSE, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.REDSTONE_WIRE, Material.SAPLING,
                    Material.SEEDS, Material.SIGN_POST, Material.STAINED_GLASS_PANE, Material.STATIONARY_LAVA, Material.STATIONARY_WATER,
                    Material.STONE_BUTTON, Material.STONE_PLATE, Material.SUGAR_CANE_BLOCK, Material.THIN_GLASS, Material.TORCH, Material.TRIPWIRE,
                    Material.TRIPWIRE_HOOK, Material.VINE, Material.WALL_SIGN, Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE
            );

            for (Material m : materials) {
                translucentMaterialsString.add(m.toString());
            }

            this.translucentMaterials = materials;

            save();
        } else {
            translucentMaterialsString.forEach(s -> translucentMaterials.add(Material.valueOf(s)));
        }
    }

    public void addToList(Material mat) {
        this.translucentMaterials.add(mat);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {

        Location from = e.getFrom();
        Location to = e.getTo();

        if (from.getX() == to.getX() && from.getZ() == to.getZ()) {
            return;
        }

        if (from.distanceSquared(to) > 64) {
            if (isCancel()) {
                e.setTo(from);
            }
            return;
        }

        Block b = to.getBlock();
        Block b1 = to.getBlock().getRelative(BlockFace.UP);

        if (b.getType() != Material.FENCE_GATE || b1.getType() != Material.FENCE_GATE) {
            return;
        }

        Gate gate = (Gate) b.getState().getData();
        Gate gate1 = (Gate) b1.getState().getData();

        if (gate.isOpen() && gate1.isOpen()) {
            // log("both gates are open.");
            return;
        }

        double fromy = -1;
        double toy = -1;
        double middle = -1;

        final Player p = e.getPlayer();
        final ReflexPlayer rp = getPlayer(p);

        switch (gate.getFacing()) {
            case NORTH:
            case SOUTH:
                fromy = from.getX();
                toy = to.getX();
                middle = from.getBlockX() + 0.5;
                break;
            case EAST:
            case WEST:
                fromy = from.getZ();
                toy = to.getZ();
                middle = from.getBlockZ() + 0.5;
                break;
            default:
                fromy = 0;
                toy = 0;
                break;
        }

        if (fromy == -1 || toy == -1 || middle == -1) {
            return;
        }

        fromy = Math.abs(fromy);
        toy = Math.abs(toy);
        middle = Math.abs(middle);

        if ((toy < middle && middle < fromy) || (fromy < middle && middle < toy)) {
            if (fail(rp, "Type 1").isCancelled()) {
                if (rp.getData().getSetBack() != null) {
                    e.setTo(rp.getData().getSetBack());
                } else {
                    e.setTo(from);
                }
            }
            return;
        }

        double plusb = middle + radi;
        double negb = middle - radi;

        if (fromy < negb) {
            if (toy >= negb) {
                if (fail(rp, "Type 2A").isCancelled()) {
                    e.setTo(from);
                }
            }
        } else if (fromy > plusb) {
            if (toy <= plusb) {
                if (fail(rp, "Type 2B").isCancelled()) {
                    e.setTo(from);
                }
            }
        }

        if (isBetween(fromy, middle, radi) ^ isBetween(toy, middle, radi)) {
            if (fail(rp, "Type 3").isCancelled()) {
                e.setTo(from);
            }
        }

    }

    public boolean isBetween(double number, double middle, double rad) {
        return middle - rad < number && number < middle + rad;
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLogin(PlayerJoinEvent e) {
        final Location location = e.getPlayer().getLocation();

        getReflex().getServer().getScheduler().runTaskLater(getReflex(), () -> {
            if (e.getPlayer().isOnline()) {
                if (e.getPlayer().getLocation().distanceSquared(location) < 1) {
                    e.getPlayer().teleport(location);
                }
            }
        }, 4L);
    }

    @EventHandler(ignoreCancelled = true)
    public void denyPhase(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ReflexPlayer rp = getPlayer(player);
        if (player.isFlying()) {
            return;
        }
        Location t = event.getTo();
        if (t.getY() > 254) {
            return;
        }
        Location f = event.getFrom();
        double distance = f.distanceSquared(t);
        if (distance == 0.0D) {
            return;
        }

        if (distance > 64.0D) {
            if (isCancel()) {
                event.setTo(f.setDirection(t.getDirection()));
            }
            return;
        }

        Block blockTo = t.getBlock();

        Material matTo = blockTo.getType();
        if ((t.getBlockX() == f.getBlockX() && t.getBlockY() == f.getBlockY() && t.getBlockZ() == f.getBlockZ())) {
            return;
        }

        if (matTo == Material.AIR) {
            return;
        }

        int topBlockX = f.getBlockX() < t.getBlockX() ? t.getBlockX() : f.getBlockX();
        int bottomBlockX = f.getBlockX() > t.getBlockX() ? t.getBlockX() : f.getBlockX();

        int topBlockY = (f.getBlockY() < t.getBlockY() ? t.getBlockY() : f.getBlockY()) + 1;
        int bottomBlockY = f.getBlockY() > t.getBlockY() ? t.getBlockY() : f.getBlockY();
        if (player.isInsideVehicle()) {
            bottomBlockY++;
        }

        int topBlockZ = f.getBlockZ() < t.getBlockZ() ? t.getBlockZ() : f.getBlockZ();
        int bottomBlockZ = f.getBlockZ() > t.getBlockZ() ? t.getBlockZ() : f.getBlockZ();

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    if (translucentMaterials.contains(f.getWorld().getBlockAt(x, y, z).getType())) {
                        continue;
                    }

                    if (y == bottomBlockY && f.getBlockY() != t.getBlockY()) {
                        continue;
                    }

                    if (fail(rp, "Solid block").isCancelled()) {
                        event.setTo(f.setDirection(t.getDirection()));
                    }
                }
            }
        }
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }
}
