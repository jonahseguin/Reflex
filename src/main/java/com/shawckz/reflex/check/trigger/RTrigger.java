/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.autoban.Autoban;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.Check;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.inspect.RInspectResult;
import com.shawckz.reflex.check.inspect.RInspectResultType;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Alert;
import com.shawckz.reflex.util.utility.ReflexCaller;
import com.shawckz.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RTrigger extends Check {

    @ConfigData("cancel")
    private boolean cancel = true;

    @ConfigData("capture-time")
    private int captureTime = 15;

    @ConfigData("autoban-freeze")
    private boolean autobanFreeze = true;

    @ConfigData("autoban")
    private boolean autoban = true;

    public RTrigger(CheckType checkType, RCheckType rCheckType) {
        super(checkType, rCheckType);
    }

    public final boolean triggerLater(ReflexPlayer player, ReflexCaller<RTriggerResult> result) {
      //  if (ReflexPerm.BYPASS.hasPerm(player.getBukkitPlayer())) return false;
        if (getCheckType().isCapture()) {
            if (!player.getCapturePlayer().isCapturing(getCheckType())) {
                if(!Reflex.getInstance().getAutobanManager().hasAutoban(player.getName())) {

                    //Add a violation level (even if they pass - to make that they failed a trigger)
                    player.addVL(getCheckType());

                    Alert alert = new Alert(player, getCheckType(), Alert.Type.TRIGGER, null, -1);
                    alert.sendAlert();

                    Reflex.getInstance().getDataCaptureManager().startCaptureTask(player, getCheckType(), captureTime, checkData -> {
                        RInspectResult inspectResult = Reflex.getInstance().getInspectManager().inspect(player, getCheckType(), checkData, captureTime);
                        handleInspect(player, inspectResult, true);
                        result.call(new RTriggerResult(this.cancel && inspectResult.getResult() == RInspectResultType.FAILED, this.cancel));
                    });
                }
            }
        }
        else {
            throw new ReflexException("This trigger must call trigger instead of triggerLater ("+getCheckType().getName()+")");
        }
        return this.cancel;
    }

    public final RTriggerResult trigger(ReflexPlayer player) {
        if (!getCheckType().isCapture()) {
            RInspectResult inspectResult = Reflex.getInstance().getInspectManager().inspect(player, getCheckType(), player.getData().copy(), 1);
            handleInspect(player, inspectResult, false);
            return new RTriggerResult(this.cancel && inspectResult.getResult() == RInspectResultType.FAILED, this.cancel);
        }
        else {
            throw new ReflexException("This trigger must call triggerLater instead of trigger ("+getCheckType().getName()+")");
        }
    }

    private void handleInspect(ReflexPlayer player, RInspectResult inspectResult, boolean capture) {
        //The inspect method handles the adding VL and alert.
        //Here, we will handle the autobanning (if necessary...)

        if(capture) {
            //Was probably sure about the result, ---> autoban
            if(autoban) {
                if (!Reflex.getInstance().getAutobanManager().hasAutoban(player.getName())) {
                    Autoban autoban = new Autoban(player, Reflex.getInstance().getReflexConfig().getAutobanTime(), getCheckType(), inspectResult.getViolation());
                    Reflex.getInstance().getAutobanManager().putAutoban(autoban);
                    autoban.run();
                }
            }
        }
        else{
            int vl = Reflex.getInstance().getInspectManager().getInspector(getCheckType()).getAutobanVL();

            if(player.getVL(getCheckType()) > vl) {
                vl *= 2;
            }

            if (player.getVL(getCheckType()) >= vl) {
                if(autoban) {
                    if (!Reflex.getInstance().getAutobanManager().hasAutoban(player.getName())) {
                        //Only if they aren't already being autobanned...
                        Autoban autoban = new Autoban(player, Reflex.getInstance().getReflexConfig().getAutobanTime(), getCheckType(), inspectResult.getViolation());
                        Reflex.getInstance().getAutobanManager().putAutoban(autoban);
                        autoban.run();
                    }
                }
            }
        }
    }

}
