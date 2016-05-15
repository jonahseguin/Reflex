/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.Check;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;

public abstract class RDataCapture extends Check {

    public RDataCapture(CheckType checkType, RCheckType rCheckType) {
        super(checkType, rCheckType);
    }

    public final void startCapturing(ReflexPlayer player) {
        player.getCapturePlayer().startCapturing(getCheckType());
    }

    public final void stopCapturing(ReflexPlayer player) {
        player.getCapturePlayer().stopCapturing(getCheckType());
    }

    public final boolean isCapturing(Player player) {
        ReflexPlayer reflexPlayer = Reflex.getInstance().getCache().getReflexPlayer(player);
        return reflexPlayer.getCapturePlayer().isCapturing(getCheckType());
    }

}
