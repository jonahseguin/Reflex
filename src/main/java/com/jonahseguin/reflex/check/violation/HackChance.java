/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.violation;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.ReflexConfig;
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
    private final ReflexConfig config;
    private double hackChance = 0.00; // % chance player is using a hack
    private int infractionsCheck = -1; // Amount of infractions for this check
    private int validReflexBans = -1; // Amount of Reflex Bans: confirmed true or not confirmed at all
    private int violationsCheck = -1; // Amount of violations for this check
    private int sessionVL = -1; // ReflexPlayer sessionVL
    private int totalVLCheck = -1; // Total VL for this check
    private int failureFrequencyCheck = -1; // How often this check is failed by the player (fails per hour)

    private int globalRecentFailsCheck = -1; // Total fails of this check between all players from last ~2 seconds

    // Ping Spiking
    private int pingBefore = -1; // 2s before
    private int pingDuring = -1; // When they failed
    private int pingAfter = -1; // 2s after

    // TPS Spiking
    private double tpsBefore = -1; // 2s before
    private double tpsDuring = -1; // When they failed
    private double tpsAfter = -1; // 2s after

    public HackChance(ReflexPlayer player, CheckType checkType, long currentMillisecond, int totalTick) {
        this.player = player;
        this.checkType = checkType;
        this.currentMillisecond = currentMillisecond;
        this.totalTick = totalTick;
        this.config = Reflex.getInstance().getReflexConfig();
    }

    public void update() {
        this.infractionsCheck = (int) player.getRecord().getInfractions().stream().filter(infraction -> infraction.getCheckType().equals(checkType)).count();
        this.validReflexBans = player.getRecord().getValidReflexBans(checkType);
        this.violationsCheck = player.getRecord().getViolationCount(checkType);
        this.sessionVL = player.getSessionVL();
        this.totalVLCheck = player.getRecord().getAllViolations(checkType).size();
        this.globalRecentFailsCheck = Reflex.getInstance().getCheckManager().getCheck(checkType).getTotalRecentFails(2);
        this.failureFrequencyCheck = player.getRecord().getFailureFrequency(checkType);
    }


    /**
     * Should be updated seperately from {@link HackChance#update()} because the 'after' values require a
     * 2s wait after fail
     */
    public void updatePingAndTps() {
        this.pingBefore = player.getPlayerPing().getAveragePing(totalTick - (2 * 20), currentMillisecond - (2 * 1000)); // 2 seconds before
        this.pingDuring = player.getPlayerPing().getAveragePing(totalTick, currentMillisecond);
        this.pingAfter = player.getPlayerPing().getAveragePing(totalTick + (2 * 20), currentMillisecond + (2 * 1000)); // 2 seconds after

        this.tpsBefore = Reflex.getInstance().getTpsHandler().getTps(totalTick - (2 * 20));
        this.tpsDuring = Reflex.getInstance().getTpsHandler().getTps(totalTick);
        this.tpsAfter = Reflex.getInstance().getTpsHandler().getTps(totalTick + (2 * 20));
    }

    public double calculate() {
        double score = 0;
        double total = 0;

        if (infractionsCheck != -1) {
            total++;
            if (infractionsCheck >= config.getHackChanceInfractionsMinimum()) {
                score++;
            }
        }

        if (validReflexBans != -1) {
            total++;
            if (validReflexBans >= config.getHackChanceValidReflexBansMinimum()) {
                score++;
            }
        }

        if (violationsCheck != -1) {
            total++;
            if (violationsCheck >= config.getHackChanceViolationsMinimum()) {
                score++;
            }
        }

        if (sessionVL != -1) {
            total++;
            if (sessionVL >= config.getHackChanceSessionVLMinimum()) {
                score++;
            }
        }

        if (totalVLCheck != -1) {
            total++;
            if (totalVLCheck >= config.getHackChanceTotalVLMinimum()) {
                score++;
            }
        }

        if (failureFrequencyCheck != -1) {
            total++;
            if (failureFrequencyCheck >= config.getHackChanceFailureFrequencyMinimum()) {
                score++;
            }
        }

        if (globalRecentFailsCheck != -1) {
            total++;
            if (globalRecentFailsCheck < config.getHackChanceGlobalRecentFailsMaximum()) {
                score++;
            } else {
                // Was likely a lag spike causing several players to fail
                score--; // weighted higher, subtract
            }
        }

        if (pingBefore != -1 && pingDuring != -1 && pingAfter != -1) {
            total++;
            double spikeBeforeDuring = spike(pingBefore, pingDuring);
            double spikeDuringAfter = spike(pingDuring, pingAfter);
            int max = config.getHackChancePingSpikeMaximum();
            if (spikeBeforeDuring >= max || spikeDuringAfter >= max) {
                score++;
            } else {
                // Likely ping spike
                score--;
            }
        }

        if (tpsBefore != -1 && tpsDuring != -1 && tpsAfter != -1) {
            total++;
            double spikeBeforeDuring = spike(tpsBefore, tpsDuring);
            double spikeDuringAfter = spike(tpsDuring, tpsAfter);
            double max = config.getHackChanceTpsSpikeMaximum();
            if (spikeBeforeDuring >= max || spikeDuringAfter >= max) {
                score++;
            } else {
                // Likely ping spike
                score--;
            }
        }

        if (total == 0) {
            this.hackChance = 0;
            return 0;
        }

        this.hackChance = Math.round(((score / total) * 100));

        return this.hackChance;
    }

    private double spike(double before, double after) {
        return Math.abs(before - after);
    }

}
