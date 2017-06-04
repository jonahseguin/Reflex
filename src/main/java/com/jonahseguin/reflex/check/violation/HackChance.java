/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.violation;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;

/**
 * Created by Jonah Seguin on Sun 2017-06-04 at 00:08.
 * Project: Reflex
 */
@Getter
public class HackChance {

    private final ReflexPlayer player;
    private final CheckType checkType;
    private final long currentMillisecond;
    private final int totalTick;
    private double hackChance = 0.00; // % chance player is using a hack
    private int infractionsCheck; // Amount of infractions for this check
    private int validReflexBans; // Amount of Reflex Bans: confirmed true or not confirmed at all
    private int violationsCheck; // Amount of violations for this check
    private int sessionVL; // ReflexPlayer sessionVL
    private int totalVLCheck; // Total VL for this check
    private int failureFrequencyCheck; // How often this check is failed by the player (todo)

    private int globalRecentFailsCheck; // Total fails of this check between all players from last ~2 seconds

    // Ping Spiking
    private int pingBefore; // 2s before
    private int pingDuring; // When they failed
    private int pingAfter; // 2s after

    // TPS Spiking
    private double tpsBefore; // 2s before
    private double tpsDuring; // When they failed
    private double tpsAfter; // 2s after

    public HackChance(ReflexPlayer player, CheckType checkType, long currentMillisecond, int totalTick) {
        this.player = player;
        this.checkType = checkType;
        this.currentMillisecond = currentMillisecond;
        this.totalTick = totalTick;
    }

    public void update() {
        this.infractionsCheck = (int) player.getRecord().getInfractions().stream().filter(infraction -> infraction.getCheckType().equals(checkType)).count();


        this.pingBefore = player.getPlayerPing().getAveragePing(totalTick - (2 * 20), currentMillisecond - (2 * 1000)); // 2 seconds before
        this.pingDuring = player.getPlayerPing().getAveragePing(totalTick, currentMillisecond);
        this.pingAfter = player.getPlayerPing().getAveragePing(totalTick + (2 * 20), currentMillisecond + (2 * 1000)); // 2 seconds after

        this.tpsBefore = Reflex.getInstance().getTpsHandler().getTps(totalTick - (2 * 20));
        this.tpsDuring = Reflex.getInstance().getTpsHandler().getTps(totalTick);
        this.tpsAfter = Reflex.getInstance().getTpsHandler().getTps(totalTick + (2 * 20));

        // todo: implement the rest of the values

    }

    // todo: add a method to calculate the % chance by factoring in all variables (double #calculate())

}
