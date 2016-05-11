/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.inspect;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.bridge.CheckType;
import lombok.Data;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah Seguin on 5/8/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Data
public abstract class RTrainer implements ReflexInput {

    private final CheckType checkType;
    private final int period;
    private final RInspectData data;
    private final NeuronOutput expectedOutput;

    private boolean started = false;
    private boolean cancelled = false;

    public void start() {
        started = true;
        cancelled = false;
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!cancelled) {
                    if (started) {
                        onFinish();
                    }
                }
                cancelled = false;
                cancel();
            }
        }.runTaskLaterAsynchronously(Reflex.getInstance(), (20 * period));
    }

    public void cancel() {
        cancelled = true;
    }

    public abstract void onFinish();

}
