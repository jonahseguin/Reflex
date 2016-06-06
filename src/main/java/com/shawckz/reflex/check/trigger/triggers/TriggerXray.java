/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.triggers;

import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.base.RTimer;
import com.shawckz.reflex.check.trigger.RTrigger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TriggerXray extends RTrigger implements RTimer {

    //TODO

    public TriggerXray() {
        super(CheckType.XRAY, RCheckType.TRIGGER);
    }

    @Override
    public void runTimer() {

    }

    @Override
    public int getCaptureTime() {
        return 0;
    }
}
