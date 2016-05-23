/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event;

import com.shawckz.reflex.check.base.Check;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;

public class ReflexCancelEvent extends ReflexEvent {

    private final Check check;
    private final CheckType checkType;
    private final RCheckType checkSource;

    public ReflexCancelEvent(Check check, CheckType checkType, RCheckType checkSource) {
        this.check = check;
        this.checkType = checkType;
        this.checkSource = checkSource;
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
}
