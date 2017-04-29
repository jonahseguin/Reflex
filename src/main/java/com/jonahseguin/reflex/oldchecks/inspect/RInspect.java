/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.inspect;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.Check;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.data.CheckData;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RInspect extends Check {

    @ConfigData("inspection-delay")
    private int inspectionDelay = 5;//To leave time for potential lag spikes to propagate - delay the inspection

    public RInspect(Reflex instance, CheckType checkType, RCheckType rCheckType) {
        super(instance, checkType, rCheckType);
    }

    public abstract RInspectResultData inspect(ReflexPlayer player, CheckData checkData, int dataPeriod);

    public abstract int getAutobanVL();

}
