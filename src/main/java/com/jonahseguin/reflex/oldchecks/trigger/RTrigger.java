/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.ban.Autoban;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.event.api.ReflexCancelEvent;
import com.jonahseguin.reflex.event.api.ReflexTriggerEvent;
import com.jonahseguin.reflex.oldchecks.base.Check;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.base.RViolation;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResult;
import com.jonahseguin.reflex.oldchecks.inspect.RInspectResultType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Alert;
import com.jonahseguin.reflex.util.utility.ReflexCaller;
import com.jonahseguin.reflex.util.utility.ReflexException;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
public abstract class RTrigger extends Check {

    @ConfigData("cancel")
    private boolean cancel = true;

    @ConfigData("ban-freeze")
    private boolean autobanFreeze = true;

    @ConfigData("autoban")
    private boolean autoban = true;

    @ConfigData("simple-oldchecks-autoban-vl")
    private int simpleAutobanVL = 3;

    public RTrigger(Reflex instance, CheckType checkType, RCheckType rCheckType) {
        super(instance, checkType, rCheckType);
    }

    public final boolean triggerLater(ReflexPlayer player, ReflexCaller<RTriggerResult> result) {
        ReflexTriggerEvent triggerEvent = new ReflexTriggerEvent(this, player, getCheckType());
        Bukkit.getServer().getPluginManager().callEvent(triggerEvent);
        if (!triggerEvent.isCancelled()) {
            ReflexCancelEvent cancelEvent = new ReflexCancelEvent(this, getCheckType(), getRCheckType(), this.cancel);
            Bukkit.getServer().getPluginManager().callEvent(cancelEvent);
            final boolean cancel = cancelEvent.isShouldCancel();

            if (getCheckType().isCapture()) {
                if (!player.getCapturePlayer().isCapturing(getCheckType())) {
                    if (!getReflex().getAutobanManager().hasAutoban(player.getName())) {

                        //Add a violation level (even if they pass - to make that they failed a trigger)
                        player.addVL(getCheckType());

                        Alert alert = new Alert(player, getCheckType(), Alert.Type.TRIGGER, null, -1);
                        alert.sendAlert();

                        getReflex().getDataCaptureManager().startCaptureTask(player, getCheckType(), getCaptureTime(), checkData -> {
                            RInspectResult inspectResult = getReflex().getInspectManager().inspect(player, getCheckType(), checkData, getCaptureTime());
                            handleInspect(player, inspectResult, true);

                            result.call(new RTriggerResult(cancel && inspectResult.getData().getType() == RInspectResultType.FAILED, cancel));
                        });
                    }
                }
            } else {
                throw new ReflexException("This trigger must call trigger instead of triggerLater (" + getCheckType().getName() + ")");
            }
            return cancel;
        } else {
            return false;
        }
    }

    public final RTriggerResult trigger(ReflexPlayer player) {
        if (!getCheckType().isCapture()) {
            ReflexTriggerEvent triggerEvent = new ReflexTriggerEvent(this, player, getCheckType());
            Bukkit.getServer().getPluginManager().callEvent(triggerEvent);
            if (!triggerEvent.isCancelled()) {
                if (!getReflex().getAutobanManager().hasAutoban(player.getName())) {
                    RInspectResult inspectResult = getReflex().getInspectManager().inspect(player, getCheckType(), player.getData().copy(), 1);
                    handleInspect(player, inspectResult, false);
                    return new RTriggerResult(this.cancel && inspectResult.getData().getType() == RInspectResultType.FAILED, this.cancel);
                } else {
                    return new RTriggerResult(false, this.cancel);
                }
            } else {
                return new RTriggerResult(false, false);
            }
        } else {
            throw new ReflexException("This trigger must call triggerLater instead of trigger (" + getCheckType().getName() + ")");
        }
    }

    private void handleInspect(ReflexPlayer player, RInspectResult inspectResult, boolean capture) {
        //The inspect method handles the adding VL and alert.
        //Here, we will handle the autobanning (if necessary...)

        if (capture) {
            //Was probably sure about the result, ---> ban
            if (autoban && inspectResult.getData().getType() == RInspectResultType.FAILED) {
                if (!getReflex().getAutobanManager().hasAutoban(player.getName())) {
                    Autoban autoban = new Autoban(player, getReflex().getReflexConfig().getAutobanTime(), getCheckType(), inspectResult.getViolation());
                    getReflex().getAutobanManager().putAutoban(autoban);
                    autoban.run();
                }
            }
        } else {
            int vl = getReflex().getInspectManager().getInspector(getCheckType()).getAutobanVL();

            if (player.getVL(getCheckType()) > vl) {
                vl *= 2;
            }

            if (player.getVL(getCheckType()) >= vl) {
                if (autoban) {
                    if (!getReflex().getAutobanManager().hasAutoban(player.getName())) {
                        //Only if they aren't already being autobanned...
                        Autoban autoban = new Autoban(player, getReflex().getReflexConfig().getAutobanTime(), getCheckType(), inspectResult.getViolation());
                        getReflex().getAutobanManager().putAutoban(autoban);
                        autoban.run();
                    }
                }
            }
        }
    }

    public SimpleCheckResult fail(ReflexPlayer player, String... detail) {
        if (player.getCapturePlayer().isCapturing(getCheckType()) || getReflex().getAutobanManager().hasAutoban(player.getName())) {
            //No failures (for this oldchecks) if capturing data (for this oldchecks)..
            return new SimpleCheckResult(false, null);
        }

        String d = null;
        if (detail != null && detail.length > 0) {
            d = detail[0];
        }

        player.addVL(getCheckType());

        RViolation violation = new RViolation(player.getUniqueId(), player.getData().copy(), getCheckType(), RCheckType.TRIGGER, player.getVL(getCheckType()));
        getReflex().getViolationCache().saveViolation(violation);

        if (!getReflex().getAutobanManager().hasAutoban(player.getName())) {
            Alert alert = new Alert(player, getCheckType(), Alert.Type.FAIL, violation, player.getVL(getCheckType()));
            if (d != null) {
                alert.setDetail(d);
            }
            alert.sendAlert();

            //Assuming this is a simple oldchecks due to the fact that the method called was #fail
            if (player.getVL(getCheckType()) >= simpleAutobanVL && isAutoban()) {
                Autoban autoban = new Autoban(player, getReflex().getReflexConfig().getAutobanTime(), getCheckType(), violation);
                getReflex().getAutobanManager().putAutoban(autoban);
                autoban.run();
            }
        }

        ReflexCancelEvent cancelEvent = new ReflexCancelEvent(this, getCheckType(), getRCheckType(), cancel);
        Bukkit.getServer().getPluginManager().callEvent(cancelEvent);
        cancel = cancelEvent.isShouldCancel();

        return new SimpleCheckResult(cancel, violation);
    }

    public abstract int getCaptureTime();

}
