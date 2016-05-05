package com.shawckz.reflex.checks;

import com.shawckz.reflex.check.Check;
import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class CheckPhase extends Check {

    public CheckPhase() {
        super(CheckType.PHASE);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e){
        if(e.isCancelled()) return;
        if(e.getTo().getBlockX() == e.getFrom().getBlockX()
                && e.getTo().getBlockY() == e.getFrom().getBlockY()
                && e.getTo().getBlockZ() == e.getFrom().getBlockZ()){
            return;
        }
        Player pl = e.getPlayer();
        ReflexPlayer p = ReflexCache.get().getAresPlayer(pl);

        if(pl.getGameMode() == GameMode.CREATIVE) return;// Players with creative mode can usually glitch into blocks
        if(pl.getAllowFlight()) return;// Players with fly can also glitch into blocks.  Not necessary
        if(pl.isInsideVehicle() || pl.getVehicle() != null) return;

        final double xDistance = e.getTo().getX() - e.getFrom().getX();
        final double zDistance = e.getTo().getZ() - e.getFrom().getZ();

        final double hDistance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);

      //  Distance distance = new Distance(e.getFrom(),e.getTo());
      //  double to = Math.round(distance.toY());

        for (int i = 0; i < (Math.round(hDistance)) + 1; i++) {
            Block block = new Location(pl.getWorld(), pl.getLocation().getX(), hDistance + i, pl.getLocation().getZ()).getBlock();
            if (block.getType() != Material.AIR && block.getType().isSolid()){
                //They moved past a solid block, likely Phase.
                if(fail(p).isCancelled()){
                    e.setTo(e.getFrom());
                }
            }
        }


    }


}
