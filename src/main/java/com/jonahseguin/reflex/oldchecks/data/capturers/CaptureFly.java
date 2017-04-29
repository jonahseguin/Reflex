/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data.capturers;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.base.RTimer;
import com.jonahseguin.reflex.oldchecks.data.RDataCapture;
import com.jonahseguin.reflex.oldchecks.data.checkdata.DataFly;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

@Getter
@Setter
public class CaptureFly extends RDataCapture implements RTimer {


    public CaptureFly(Reflex instance) {
        super(instance, CheckType.FLY, RCheckType.DATA);
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
            ReflexPlayer rp = getPlayer(p);
            DataFly data = getData(rp);

            //YPS
            double yDifference = e.getTo().getY() - e.getFrom().getY();
            data.setYps(data.getYps() + yDifference);

            //BPS
            final double xDistance = e.getTo().getX() - e.getFrom().getX();
            final double zDistance = e.getTo().getZ() - e.getFrom().getZ();
            final double distance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
            data.setBps(data.getBps() + distance);

            //POSITIVE VELOCITY
            if (e.getTo().getY() >= e.getFrom().getY()) {
                data.setHasPositiveVelocity(true);
            }

            //GROUND TIME
            if (rp.getData().isOnGround()) {
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

                    //Update ground time again, in case they stood still
                    if (p.getData().isOnGround()) {
                        data.setLastGroundTime(System.currentTimeMillis());
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
