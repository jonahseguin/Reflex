/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.trigger;

import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.player.ReflexPlayer;
import com.shawckz.reflex.inspect.InspectHandler;
import com.shawckz.reflex.prevent.check.CheckViolation;
import com.shawckz.reflex.util.ReflexException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class InspectTriggerHandler {

    private static final Map<String, InspectTrigger> triggers = new HashMap<>();

    /*
    purpose of inspect triggering is - a preventative (CHECK) check that is triggerable (CheckType),
    when failed will trigger a neural inspection type check for X time
     */

    public static boolean canTrigger(ReflexPlayer player) {
        if(triggers.containsKey(player.getUniqueId())) {
            if(triggers.get(player.getUniqueId()).expired()) {
                triggers.remove(player.getUniqueId());
                return true;
            }
        }
        return false;
    }

    public static void trigger(ReflexPlayer player, CheckType triggeredBy, CheckViolation violation) {
        if(!canTrigger(player)) {
            throw new ReflexException("Cannot trigger a player that already has a trigger running");
        }

        InspectHandler.test();

    }

}
