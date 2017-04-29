/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.data.checkdata;

import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.oldchecks.data.CheckData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
@CollectionName(name = "reflex_checkdata")
public class DataVClip extends CheckData {

    private int vclipAttempts = 0;
    private int vclipDistance = 0;

    private boolean triedVClip = false;
    private int vclipY = -1;
    private Location lastVClipLocation = null;

}
