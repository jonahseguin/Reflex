/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.violation;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.backend.database.mongo.annotations.MongoColumn;
import com.jonahseguin.reflex.ban.Autoban;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckFail;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonah Seguin on Sat 2017-05-13 at 10:14.
 * Project: Reflex
 */
@Getter
public class PlayerRecord extends AutoMongo {

    private final Reflex reflex;
    /*

    A ReflexPlayer's "record" -- will store temporary and permanent violations

    Easily access amounts of violations, violations for a specific check, most recent violations (within x time), etc.

     */
    private ReflexPlayer reflexPlayer;
    @MongoColumn(name = "infractions")
    // TODO: DatabaseSerializer
    private Set<Infraction> infractions = new HashSet<>();
    private Set<String> violations = new HashSet<>(); // <CheckViolation ID> (Non persistent)
    private Map<CheckType, Integer> vl = new HashMap<>();
    private Map<CheckType, Integer> preVL = new HashMap<>(); // Non persistent

    public PlayerRecord() {
        // AutoMongo
        this.reflex = getReflex();
    }

    public PlayerRecord(ReflexPlayer reflexPlayer) {
        this.reflexPlayer = reflexPlayer;
        this.reflex = getReflex();
    }

    public Set<String> getViolationIDs() {
        return violations;
    }

    public Set<CheckViolation> getViolations() {
        Set<CheckViolation> v = new HashSet<>();
        violations.forEach(violation -> {
            CheckViolation cv = getReflex().getViolationCache().getViolation(violation);
            if (cv != null) {
                v.add(cv);
            }
        });
        return v;
    }

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

    public int getViolationCount(CheckType checkType) {
        return getViolations(checkType).size();
    }

    private CheckViolation addCheckViolation(CheckType checkType, String detail, boolean infraction) {
        CheckViolation violation =
                new CheckViolation(reflexPlayer, System.currentTimeMillis(), -1, checkType, getViolationCount(checkType), detail, infraction);
        getReflex().getViolationCache().cacheViolation(violation);
        return violation;
    }

    public CheckViolation addViolation(CheckType checkType, String detail) {
        Check check = getReflex().getCheckManager().getCheck(checkType);
        int violationCount = getViolationCount(checkType);

        CheckViolation violation;

        if (violationCount >= check.getInfractionVL()) {
            // Infraction; permanent infraction
            // Auto-ban if allowed
            violation = addCheckViolation(checkType, detail, true);

            final Infraction infraction = new Infraction(reflexPlayer, checkType, violationCount, detail);

            if (check.isAutoban() && !getReflex().getAutobanManager().hasAutoban(reflexPlayer)) {
                Autoban autoban = new Autoban(reflexPlayer, getReflex().getReflexConfig().getAutobanTime(), checkType, infraction);
                autoban.run();
                getReflex().getAutobanManager().putAutoban(autoban);
            }

            // Reset violations (except for the violation we just added)


            new BukkitRunnable() {
                @Override
                public void run() {
                    infraction.update();
                }
            }.runTaskAsynchronously(getReflex());

        } else {
            violation = addCheckViolation(checkType, detail, false);
        }

        if (!getReflex().getAutobanManager().hasAutoban(reflexPlayer)) {
            getReflex().getAlertManager().alert(violation);
        }

        return violation;
    }

    public void resetViolations(CheckType checkType, CheckViolation violation) {

    }

    public int getRecentFails(Check check, long timeFrame) {
        int count = 0;
        long timeMin = (System.currentTimeMillis() - timeFrame);
        for (CheckFail fail : check.getFails()) {
            if (fail.getTime() >= timeMin) {
                count++;
            }
        }
        return count;
    }

    public int getRecentFails(ReflexPlayer player, Check check, long timeFrame) {
        int count = 0;
        long timeMin = (System.currentTimeMillis() - timeFrame);
        for (CheckFail fail : check.getFails()) {
            if (fail.getReflexPlayer().getUniqueId().equalsIgnoreCase(player.getUniqueId())) {
                if (fail.getTime() >= timeMin) {
                    count++;
                }
            }
        }
        return count;
    }

}
