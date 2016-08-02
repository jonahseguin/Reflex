/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.check.base.Check;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;

public class ReflexCancelEvent extends ReflexAPIEvent {

    private final Check check;
    private final CheckType checkType;
    private final RCheckType checkSource;
    private boolean shouldCancel;

    public ReflexCancelEvent(Check check, CheckType checkType, RCheckType checkSource, boolean shouldCancel) {
        this.check = check;
        this.checkType = checkType;
        this.checkSource = checkSource;
        this.shouldCancel = shouldCancel;
    }

    public Check getCheck() {
        return check;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public RCheckType getCheckSource() {
        return checkSource;
    }

    public boolean isShouldCancel() {
        return shouldCancel;
    }

    public void setShouldCancel(boolean shouldCancel) {
        this.shouldCancel = shouldCancel;
    }
}
