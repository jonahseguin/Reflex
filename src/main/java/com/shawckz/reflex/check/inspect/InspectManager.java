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
import com.shawckz.reflex.check.inspect.inspectors.InspectAutoClick;
import com.shawckz.reflex.check.inspect.inspectors.InspectFastBow;
import com.shawckz.reflex.check.inspect.inspectors.InspectVClip;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Alert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InspectManager {

    private ConcurrentMap<CheckType, RInspect> inspectors = new ConcurrentHashMap<>();

    public InspectManager(Reflex instance) {

    }

    public void setup() {
        //TODO: Register inspectors

        register(new InspectAutoClick());
        register(new InspectFastBow());
        register(new InspectVClip());

        inspectors.values().stream().forEach(RInspect::setupConfig);
    }

    public RInspect getInspector(CheckType checkType) {
        return inspectors.get(checkType);
    }

    public RInspectResult inspect(final ReflexPlayer player, final CheckType checkType, final CheckData data, final int dataPeriod) {
        return inspectInternal(player, checkType, data, dataPeriod);
    }

    private RInspectResult inspectInternal(ReflexPlayer player, CheckType checkType, CheckData data, int dataPeriod) {
        final RInspect inspector = getInspector(checkType);
        RInspectResultType resultType = inspector.inspect(player, data, dataPeriod);
        RViolation violation = null;
        if (resultType == RInspectResultType.FAILED && !Reflex.getInstance().getAutobanManager().hasAutoban(player.getName())) {
            //Only make a violation if they FAIL the inspection - also alert
            violation = new RViolation(player.getUniqueId(), data, checkType, RCheckType.INSPECT);
            Reflex.getInstance().getViolationCache().saveViolation(violation);

            player.addVL(inspector.getCheckType());

            if (checkType.isCapture()) {
                Alert alert = new Alert(player, checkType, resultType.translateToAlertType(), violation, player.getVL(inspector.getCheckType()));
                alert.sendAlert();
            }
            else {
                Alert alert = new Alert(player, checkType, Alert.Type.FAIL, violation, player.getVL(inspector.getCheckType()));
                alert.sendAlert();
            }
        }
        else if(resultType == RInspectResultType.PASSED) {
            Alert alert = new Alert(player, checkType, resultType.translateToAlertType(), violation, player.getVL(inspector.getCheckType()));
            alert.sendAlert();
        }
        return new RInspectResult(resultType, violation);
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
