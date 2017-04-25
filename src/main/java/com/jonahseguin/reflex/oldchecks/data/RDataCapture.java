/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.Check;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;

public abstract class RDataCapture extends Check {

    public RDataCapture(Reflex instance, CheckType checkType, RCheckType rCheckType) {
        super(instance, checkType, rCheckType);
    }

    public final void startCapturing(ReflexPlayer player) {
        player.getCapturePlayer().startCapturing(getCheckType());
    }

    public final void stopCapturing(ReflexPlayer player) {
        player.getCapturePlayer().stopCapturing(getCheckType());
    }

    public final boolean isCapturing(Player player) {
        ReflexPlayer reflexPlayer = getReflex().getCache().getReflexPlayer(player);
        return reflexPlayer.getCapturePlayer().isCapturing(getCheckType());
    }

}
