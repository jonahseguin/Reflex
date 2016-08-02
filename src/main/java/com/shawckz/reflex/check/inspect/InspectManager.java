/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.inspect;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.base.RViolation;
import com.shawckz.reflex.check.data.CheckData;
import com.shawckz.reflex.check.inspect.inspectors.*;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Alert;
import com.shawckz.reflex.util.utility.ReflexException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.scheduler.BukkitRunnable;

public class InspectManager {

    private ConcurrentMap<CheckType, RInspect> inspectors = new ConcurrentHashMap<>();

    public InspectManager(Reflex instance) {

    }

    public void setup() {
        //TODO: Register inspectors

        register(new InspectAutoClick());
        register(new InspectFastBow());
        register(new InspectVClip());
        register(new InspectRegen());
        register(new InspectReach());
        register(new InspectFly());

        inspectors.values().stream().forEach(RInspect::setupConfig);
    }

    public RInspect getInspector(CheckType checkType) {
        return inspectors.get(checkType);
    }

    public ConcurrentMap<CheckType, RInspect> getInspectors() {
        return inspectors;
    }

    public RInspectResult inspect(final ReflexPlayer player, final CheckType checkType, final CheckData data, final int dataPeriod) {
        return inspectInternal(player, checkType, data, dataPeriod);
    }

    private RInspectResult inspectInternal(ReflexPlayer player, CheckType checkType, CheckData data, int dataPeriod) {
        final RInspect inspector = getInspector(checkType);
        RInspectResultData resultData = inspector.inspect(player, data, dataPeriod);
        RInspectResultType resultType = resultData.getType();
        RViolation violation = null;
        Alert alert = null;
        if (resultType == RInspectResultType.FAILED && !Reflex.getInstance().getAutobanManager().hasAutoban(player.getName())) {
            //Only make a violation if they FAIL the inspection - also alert
            player.addVL(inspector.getCheckType());
            violation = new RViolation(player.getUniqueId(), data, checkType, RCheckType.INSPECT, player.getVL(inspector.getCheckType()));
            Reflex.getInstance().getViolationCache().cacheViolation(violation);

            if (checkType.isCapture()) {
                alert = new Alert(player, checkType, resultType.translateToAlertType(), violation, player.getVL(inspector.getCheckType()));
                if (resultData.getDetail() != null) {
                    alert.setDetail(resultData.getDetail());
                }
            }
            else {
                alert = new Alert(player, checkType, Alert.Type.FAIL, violation, player.getVL(inspector.getCheckType()));
                if (resultData.getDetail() != null) {
                    alert.setDetail(resultData.getDetail());
                }
            }
        }
        else if (resultType == RInspectResultType.FAILED && Reflex.getInstance().getAutobanManager().hasAutoban(player.getName())) {
            throw new ReflexException("Should not fail check while having an autoban");
        }
        else if (resultType == RInspectResultType.PASSED) {
            violation = new RViolation(player.getUniqueId(), data, checkType, RCheckType.INSPECT, player.getVL(inspector.getCheckType()));
            Reflex.getInstance().getViolationCache().cacheViolation(violation);

            if (checkType.isCapture()) {
                alert = new Alert(player, checkType, resultType.translateToAlertType(), violation, player.getVL(inspector.getCheckType()));
                if (resultData.getDetail() != null) {
                    alert.setDetail(resultData.getDetail());
                }
            }
        }
        else {
            throw new ReflexException("Unknown action (ResultType) for inspectInternal: " + resultType.toString());
        }

        final RViolation finalViolation = violation;

        final RInspectResult result = new RInspectResult(resultData, finalViolation, dataPeriod);

        if (alert != null) {
            if (alert.getType() == Alert.Type.INSPECT_FAIL || alert.getType() == Alert.Type.INSPECT_PASS) {
                alert.setInspectResult(result);
            }

            alert.sendAlert();
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                data.update();
                result.update();
                finalViolation.update();
            }
        }.runTaskAsynchronously(Reflex.getInstance());
        return result;
    }

    public void register(RInspect inspect) {
        inspectors.put(inspect.getCheckType(), inspect);
        inspect.setEnabled(inspect.isEnabled());
        if (inspect instanceof RTimer) {
            RTimer timer = (RTimer) inspect;
            Reflex.getInstance().getReflexTimer().registerTimer(timer);
        }
    }

}
