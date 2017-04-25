/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.utility.ReflexException;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class CaptureTask {

    private final ReflexPlayer player;
    private final CheckType checkType;
    private final int seconds;
    private boolean running = false;
    private boolean stopped = false;

    public CaptureTask(ReflexPlayer player, CheckType checkType, int seconds) {
        this.player = player;
        this.checkType = checkType;
        this.seconds = seconds;
    }

    public void start() {
        if (!running) {
            running = true;
            stopped = false;

            if (!player.getCapturePlayer().isCapturing(checkType)) {
                player.getCapturePlayer().startCapturing(checkType);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!stopped) {
                            if (player.getCapturePlayer().isCapturing(checkType)) {
                                CheckData data = player.getCapturePlayer().getData(checkType);
                                player.getCapturePlayer().stopCapturing(checkType);
                                onFinish(data);
                            }
                            else {
                                throw new ReflexException("Finished CaptureTask but player was not capturing");
                            }
                        }
                        cancel();
                    }
                }.runTaskLaterAsynchronously(Reflex.getInstance(), (seconds * 20));
            }
            else {
                throw new ReflexException("Cannot start CaptureTask (player is already capturing)");
            }
        }
        else {
            throw new ReflexException("Cannot start CaptureTask (task already running)");
        }
    }

    public CheckData stop() {
        if (!stopped) {
            stopped = true;
            if (player.getCapturePlayer().isCapturing(checkType)) {
                CheckData data = player.getCapturePlayer().getData(checkType);
                player.getCapturePlayer().stopCapturing(checkType);
                onFinish(data);
                return data;
            }
            else {
                throw new ReflexException("Cannot stop CaptureTask (player is not capturing)");
            }
        }
        else {
            throw new ReflexException("Cannot stop CaptureTask (not running)");
        }
    }

    public abstract void onFinish(CheckData data);

}
