package com.jonahseguin.reflex.check.checks.movement;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Jonah on 6/8/2017.
 */
public class CheckStep extends Check {

    @ConfigData("minimum-step-y")
    private double minStepY = 0.9;

    @ConfigData("minimum-attempts")
    private int minAttempts = 3;

    public CheckStep(Reflex reflex) {
        super(reflex, CheckType.STEP);
    }

    private boolean isOnGround(Player player) {
        ReflexPlayer rp = getPlayer(player);
        if (rp.getData().isOnClimbable(player)) {
            return false;
        }
        if (player.getVehicle() != null) {
            return false;
        }
        Material type = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        Location pLoc = player.getLocation().clone();
        pLoc.setY(pLoc.getY() - 0.5);
        type = pLoc.getBlock().getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        pLoc = player.getLocation().clone();
        pLoc.setY(pLoc.getY() + 0.5);
        type = pLoc.getBlock().getRelative(BlockFace.DOWN).getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        if (rp.getData().isBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER})) {
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMoveStep(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final ReflexPlayer reflexPlayer = getPlayer(player);

        if (this.isOnGround(player)) {
            if (!player.getAllowFlight()) {
                if (!reflexPlayer.getData().isSlabsNear(player.getLocation())) {
                    if (!player.hasPotionEffect(PotionEffectType.JUMP)) {
                        if (reflexPlayer.getData().lastVelocity == null && reflexPlayer.getData().lastVelocityTime == 0) {
                            double yDistance = event.getTo().getY() - event.getFrom().getY();
                            if (yDistance > minStepY) {
                                reflexPlayer.addPreVL(getCheckType());
                                if (reflexPlayer.getPreVL(getCheckType()) >= minAttempts) {
                                    fail(reflexPlayer, Math.round(yDistance) + " y dist.").cancelIfAllowed(event);
                                    reflexPlayer.setPreVL(getCheckType(), 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String description() {
        return "Detects when a player 'steps' up a block without actually jumping.";
    }


}
