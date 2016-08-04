/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.inspect;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.configuration.annotations.ConfigData;
import com.shawckz.reflex.check.base.Check;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.data.CheckData;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
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
