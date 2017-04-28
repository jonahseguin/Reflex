/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:35.
 * Project: Reflex
 */
@Data
public class CheckViolation {

    private final String id = UUID.randomUUID().toString();
    private final ReflexPlayer reflexPlayer;
    private final long time;
    @NonNull private long expiryTime;
    private final CheckType checkType;
    private final int vl;

    public static CheckViolation emptyViolation(ReflexPlayer player, CheckType checkType) {
        return new CheckViolation(player, System.currentTimeMillis(), System.currentTimeMillis(), checkType, -1);
    }


}