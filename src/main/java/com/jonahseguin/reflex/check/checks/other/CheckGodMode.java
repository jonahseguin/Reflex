/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.other;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.event.packet.ReflexPacketKeepAliveEvent;
import com.jonahseguin.reflex.oldchecks.base.RTimer;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;
import org.bukkit.event.EventHandler;

import java.text.DecimalFormat;

/**
 * Created by Jonah Seguin on Mon 2017-05-08 at 20:21.
 * Project: Reflex
 */
public class CheckGodMode extends Check implements RTimer {

    private final DecimalFormat df = new DecimalFormat("##.##");
    @ConfigData("keep-alive-packet-max-per-second")
    private int maxKeepAlivePacketsPerSecond = 20;
    @ConfigData("minimum-attempts")
    private int minAttempts = 3;

    public CheckGodMode(Reflex reflex) {
        super(reflex, CheckType.GOD_MODE);
    }

    @Override
    public void runTimer() {
        Reflex.getReflexPlayers().forEach(rp -> {
            if (rp.getData().keepAlivePackets > calculateMaxPackets()) {
                rp.addAlertVL(getCheckType());
                if (rp.getAlertVL(getCheckType()) >= minAttempts) {
                    fail(rp, rp.getData().keepAlivePackets + " > " + df.format(calculateMaxPackets() + "pps"));
                }
            }
        });
    }

    @EventHandler
    public void onGodModeKeepAlive(final ReflexPacketKeepAliveEvent event) {
        final ReflexPlayer rp = event.getPlayer();
        rp.getData().keepAlivePackets++;
        rp.getData().lastKeepAlivePacket = System.currentTimeMillis();
    }

    private double calculateMaxPackets() {
        return Lag.getTPS() * 20 / maxKeepAlivePacketsPerSecond;
    }

}
