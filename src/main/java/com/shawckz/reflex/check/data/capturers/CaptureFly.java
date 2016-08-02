/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data.capturers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.data.RDataCapture;
import com.shawckz.reflex.check.data.checkdata.DataFly;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

@Getter
@Setter
public class CaptureFly extends RDataCapture implements RTimer {


    public CaptureFly() {
        super(CheckType.FLY, RCheckType.DATA);
    }

    //yps, bps, hasPositiveVelocity, lastGroundTime

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (isCapturing(p)) {
            if (p.getAllowFlight()) {
                return;
            }
            ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);
            DataFly data = getData(rp);
            double yDifference = e.getTo().getY() - e.getFrom().getY();
            data.setYps(data.getYps() + yDifference);

            final double xDistance = e.getTo().getX() - e.getFrom().getX();
            final double zDistance = e.getTo().getZ() - e.getFrom().getZ();

            final double distance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);

            data.setBps(data.getBps() + distance);

            if (e.getTo().getY() >= e.getFrom().getY()) {
                data.setHasPositiveVelocity(true);
            }

            Block b = p.getLocation().clone().subtract(0, 1, 0).getBlock();

            if (p.isOnGround() || b.isLiquid() || b.getType().isSolid()) {
                data.setLastGroundTime(System.currentTimeMillis());
            }
        }
    }

    private DataFly getData(ReflexPlayer reflexPlayer) {
        return (DataFly) reflexPlayer.getCapturePlayer().getData(getCheckType());
    }

    @Override
    public void runTimer() {
        Reflex.getReflexPlayers().forEach(p -> {
            Player pl = p.getBukkitPlayer();
            if (p.getCapturePlayer().isCapturing(getCheckType())) {
                if (!pl.getAllowFlight()) {
                    DataFly data = getData(p);
                    if (data.getYps() > data.getPeakYps()) {
                        data.setPeakYps(data.getYps());
                    }

                    if (data.getLastGroundTime() != -1) {
                        int airTime = (int) (System.currentTimeMillis() - data.getLastGroundTime()) / 1000;
                        if (airTime > data.getPeakAirTime()) {
                            data.setPeakAirTime(airTime);
                        }
                    }

                    data.setBps(0);
                    data.setYps(0);
                }
            }
        });
    }
}
