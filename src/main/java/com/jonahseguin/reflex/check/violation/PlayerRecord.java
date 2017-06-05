/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.violation;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.jonahseguin.reflex.backend.database.mongo.annotations.MongoColumn;
import com.jonahseguin.reflex.ban.Autoban;
import com.jonahseguin.reflex.ban.ReflexBan;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckFail;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.serial.InfractionSetSerializer;
import com.jonahseguin.reflex.util.utility.ReflexCaller;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Jonah Seguin on Sat 2017-05-13 at 10:14.
 * Project: Reflex
 */
@Getter
@CollectionName(name = "reflex_playerrecords")
public class PlayerRecord extends AutoMongo {

    private final Reflex reflex;
    /*

    A ReflexPlayer's "record" -- will store temporary and permanent violations

    Easily access amounts of violations, violations for a specific check, most recent violations (within x time), etc.

    TODO: Autoban count (per check), top VLs (per check),

     */
    @Setter
    private ReflexPlayer reflexPlayer;
    @MongoColumn(name = "infractions")
    @DatabaseSerializer(serializer = InfractionSetSerializer.class)
    private Set<Infraction> infractions = new HashSet<>();

    private Set<String> violations = new HashSet<>(); // <CheckViolation ID> (Non persistent)
    private Map<CheckType, Integer> preVL = new HashMap<>(); // Non persistent

    public PlayerRecord() {
        // AutoMongo
        this.reflex = Reflex.getInstance();
    }

    public PlayerRecord(ReflexPlayer reflexPlayer) {
        this.reflexPlayer = reflexPlayer;
        this.reflex = Reflex.getInstance();
    }

    public Set<String> getViolationIDs() {
        return violations;
    }

    /**
     * Includes INVALID violations (ones that have been reset)
     */
    public Set<CheckViolation> getAllViolations() {
        Set<CheckViolation> v = new HashSet<>();
        violations.forEach(violation -> {
            CheckViolation cv = getReflex().getViolationCache().getViolation(violation);
            if (cv != null) {
                v.add(cv);
            }
        });
        return v;
    }

    /**
     * Include INVALID violations (ones that have been reset)
     */
    public Set<CheckViolation> getAllViolations(CheckType checkType) {
        Set<CheckViolation> v = getAllViolations();
        Set<CheckViolation> filtered = new HashSet<>();
        for (CheckViolation violation : v) {
            if (violation.getCheckType().equals(checkType)) {
                filtered.add(violation);
            }
        }
        return filtered;
    }

    /**
     * Does not include INVALID violations
     */
    public Set<CheckViolation> getViolations() {
        Set<CheckViolation> v = new HashSet<>();
        violations.forEach(violation -> {
            CheckViolation cv = getReflex().getViolationCache().getViolation(violation);
            if (cv != null && cv.isValid()) {
                v.add(cv);
            }
        });
        return v;
    }

    /**
     * Does not include INVALID violations
     */
    public Set<CheckViolation> getViolations(CheckType checkType) {
        Set<CheckViolation> v = getViolations();
        Set<CheckViolation> filtered = new HashSet<>();
        for (CheckViolation violation : v) {
            if (violation.getCheckType().equals(checkType)) {
                filtered.add(violation);
            }
        }
        return filtered;
    }

    // Valid violations only
    public int getViolationCount(CheckType checkType) {
        return getViolations(checkType).size();
    }

    private CheckViolation addCheckViolation(CheckType checkType, String detail) {
        long expiryTime = System.currentTimeMillis() + getReflex().getViolationCache().getViolationCacheExpiryTimeMS();
        HackChance hackChance = new HackChance(reflexPlayer, checkType, reflex.getTpsHandler().currentMillisecond(), reflex.getTpsHandler().getTotalTick());
        CheckViolation violation = new CheckViolation(reflexPlayer, System.currentTimeMillis(), checkType, getViolationCount(checkType) + 1, detail, hackChance, false, expiryTime);
        getReflex().getViolationCache().cacheViolation(violation);
        return violation;
    }

    public void addViolation(CheckType checkType, final String detail, ReflexCaller<CheckViolation> caller) {
        final Check check = getReflex().getCheckManager().getCheck(checkType);

        final CheckViolation violation = addCheckViolation(checkType, detail);

        reflexPlayer.setSessionVL(reflexPlayer.getSessionVL() + 1);

        final HackChance hackChance = violation.getHackChance();
        hackChance.update();

        reflex.getReflexScheduler().asyncDelayedTask(() -> {
            String newDetail = detail;
            hackChance.updatePingAndTps(); // Update ping and tps (2-3s) later to allow for calculation of after values
            hackChance.calculate(); // Calculate hack chance % using data

            violation.setHackChancePassed(hackChance.getHackChance() >= check.getMinimumHackChanceAlert());

            int violationCount = getViolationCount(checkType);

            if (violationCount >= check.getInfractionVL()) { // It is now an infraction
                newDetail += ChatColor.RED + " [INF]";
                // Infraction; permanent infraction
                // Auto-ban if allowed
                violation.setInfraction(true);

                final Infraction infraction = new Infraction(reflexPlayer, checkType, violationCount, newDetail);

                if (check.isAutoban() && !getReflex().getAutobanManager().hasAutoban(reflexPlayer)) {
                    // Auto-ban the player (infraction --> auto-ban)
                    Autoban autoban = new Autoban(reflexPlayer, getReflex().getReflexConfig().getAutobanTime(), checkType, infraction);
                    autoban.run();
                    getReflex().getAutobanManager().putAutoban(autoban);
                }
                reflex.getReflexScheduler().asyncTask(infraction::update); // Update the infraction async

            } else {
                violation.setInfraction(false); // Not an infraction
            }

            if (hackChance.getHackChance() >= check.getMinimumHackChanceAlert()) {
                if (!getReflex().getAutobanManager().hasAutoban(reflexPlayer)) {
                    // If not auto-banning, create an alert
                    getReflex().getAlertManager().alert(violation);
                }
            }

            caller.call(violation);
        }, 60L); // 3 second delay to allow for Lag calculations

    }

    public void resetViolations(CheckType checkType) {
        Set<CheckViolation> allV = getViolations(checkType);
        for (CheckViolation violation : allV) {
            violation.setValid(false);
        }
    }

    public int getRecentFails(Check check, long timeFrame) {
        int count = 0;
        long timeMin = (System.currentTimeMillis() - timeFrame);
        for (CheckFail fail : check.getFails()) {
            if (fail.getReflexPlayer().getUniqueId().equalsIgnoreCase(reflexPlayer.getUniqueId())) {
                if (fail.getTime() >= timeMin) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getValidReflexBans(CheckType checkType) {
        return reflex.getBanManager().getBans(reflexPlayer.getUniqueId())
                .stream()
                .filter(reflexBan -> reflexBan.getCheckType().equals(checkType) && bannedCorrectly(reflexBan))
                .collect(Collectors.toSet())
                .size();
    }

    private boolean bannedCorrectly(ReflexBan reflexBan) {
        return reflexBan.isBanned() || (reflexBan.isBannedCorrectly() && reflexBan.isConfirmed());
    }

    /**
     * How often (fails per hour) that this check is failed by this player
     */
    public int getFailureFrequency(CheckType checkType) {
        return getRecentFails(reflex.getCheckManager().getCheck(checkType), (1000 * 60 * 60));
    }


}
