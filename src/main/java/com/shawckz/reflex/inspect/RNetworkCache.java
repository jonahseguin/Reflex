/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.inspect;

import com.shawckz.reflex.bridge.CheckType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonah Seguin on 5/8/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class RNetworkCache {

    private static final Map<CheckType, ReflexNetwork> networks = new HashMap<>();

    public static ReflexNetwork getNetwork(CheckType checkType) {
        if(!networks.containsKey(checkType)) {
            return getNewNetwork(checkType);
        }
        return networks.get(checkType);
    }

    public static void registerNetwork(CheckType checkType, ReflexNetwork network) {
        networks.put(checkType, network);
    }

    public static ReflexNetwork getNewNetwork(CheckType checkType) {
        ReflexNetwork network = new ReflexNetwork();
        registerNetwork(checkType, network);
        return network;
    }

}
