/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.violation;

import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

/**
 * Created by Jonah Seguin on Tue 2017-05-16 at 18:54.
 * Project: Reflex
 */
public interface Violation {

    ViolationType getViolationType();

    String getId();

    ReflexPlayer getReflexPlayer();

    long getTime();

    CheckType getCheckType();

    String getDetail();

    long getExpiryTime();

    int getVl();

}
