/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

/**
 * Created by Jonah Seguin on Fri 2017-04-28 at 01:25.
 * Project: Reflex
 */
public interface Alert {

    void sendAlert();

    String getId();

    ReflexPlayer getReflexPlayer();

    String getDetail();

    int getVl();

    CheckType getCheckType();

    AlertType getAlertType();

}
