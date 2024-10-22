/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.violation;

import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:35.
 * Project: Reflex
 */
@Data
public class CheckViolation implements Violation {

    private final String id = UUID.randomUUID().toString();
    private final ReflexPlayer reflexPlayer;
    private final long time;
    private final CheckType checkType;
    private final int vl;
    private final String detail;
    @NonNull
    private HackChance hackChance;
    @NonNull
    private boolean infraction;
    @NonNull
    private long expiryTime;
    private boolean valid = true;
    private boolean hackChancePassed = false;

    @Override
    public ViolationType getViolationType() {
        return ViolationType.TEMPORARY;
    }
}
