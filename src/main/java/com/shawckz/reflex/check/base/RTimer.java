/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.base;

/**
 * Method {@link RTimer#runTimer()} called once per second in any class that extends {@link com.shawckz.reflex.check.base.Check} and is registered by a manager in one
 * of the 3 Check Types or stages ({@link com.shawckz.reflex.check.base.RCheckType}).
 * See {@link com.shawckz.reflex.check.base.RTimer}
 */
public interface RTimer {

    void runTimer();

}
