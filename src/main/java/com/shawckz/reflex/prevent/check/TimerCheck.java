package com.shawckz.reflex.prevent.check;

import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.player.ReflexPlayer;

public abstract class TimerCheck extends Check {

    public TimerCheck(final CheckType checkType) {
        super(checkType);
    }

    public abstract void check(ReflexPlayer player);//This method will be called ONCE every 20 ticks; or once a second.
                                                  //Used for checks such as speed that need to check something on a
                                                  //Per-second basis

}
