/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.inspect;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.util.ReflexException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class ReflexNeuronHandler {

    private final Map<CheckType, Neuron> neurons = new HashMap<>();

    public ReflexNeuronHandler(Reflex instance) {

    }

    public Neuron getNeuron(CheckType checkType) {
        if(!neurons.containsKey(checkType)) {
            throw new ReflexException("Neuron is not registered for CheckType " + checkType.getName());
        }
        return neurons.get(checkType);
    }

}
