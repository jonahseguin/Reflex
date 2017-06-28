/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

/**
 * Method {@link RTimer#runTimer()} called once per second in any class that extends {@link Check}
 * See {@link RTimer}
 */
public interface RTimer {

    void runTimer();

}
