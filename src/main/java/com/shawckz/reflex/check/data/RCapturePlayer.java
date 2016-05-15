/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.data;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.utility.ReflexException;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RCapturePlayer {

    private final String player;
    private final Map<CheckType, CheckData> capturing = new HashMap<>();

    public RCapturePlayer(ReflexPlayer reflexPlayer) {
        this.player = reflexPlayer.getName();
    }

    public ReflexPlayer getPlayer() {
        Player p = Bukkit.getPlayerExact(player);
        if (p != null) {
            //we only want to bother if the player is actually online
            return Reflex.getInstance().getCache().getReflexPlayer(p);
        }
        return null;
    }

    public void startCapturing(CheckType checkType) {
        if (!isCapturing(checkType)) {
            try {
                CheckData data = checkType.getData().newInstance();
                capturing.put(checkType, data);
            }
            catch (IllegalAccessException | InstantiationException ex) {
                throw new ReflexException("Could not instantiate CheckData for capture", ex);
            }
        }
    }

    public CheckData stopCapturing(CheckType checkType) {
        if (isCapturing(checkType)) {
            CheckData data = getData(checkType);
            capturing.remove(checkType);
            return data;
        }
        throw new ReflexException("Cannot stop capturing if not already capturing");
    }

    public boolean isCapturing(CheckType checkType) {
        return capturing.containsKey(checkType);
    }

    public CheckData getData(CheckType checkType) {
        if (isCapturing(checkType)) {
            return capturing.get(checkType);
        }
        throw new ReflexException("Cannot get data for capturing if not capturing");
    }

}
