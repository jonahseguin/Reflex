/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Data;

/**
 * Created by Jonah Seguin on Sun 2017-05-14 at 17:03.
 * Project: Reflex
 */
@Data
public class CheckFail {

    private final long time;
    private final ReflexPlayer reflexPlayer;


}
