/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.oldchecks.base.RTimer;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:54.
 * Project: Reflex
 */
public class AlertManager implements RTimer {

    public static final int MAX_ALERTS_PER_SECOND = 2;

    private final ConcurrentLinkedQueue<AlertGroup> alertQueue = new ConcurrentLinkedQueue<>();

    public AlertManager() {
    }

    @Override
    public void runTimer() {
        // once per second, 2 alerts per second

        for (int i = 0; i < 2; i++) {

        }
    }



}
