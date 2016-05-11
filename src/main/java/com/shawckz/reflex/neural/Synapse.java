/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.neural;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
@Setter
public class Synapse {

    //Connection between two neurons
    private Neuron source;// Source --> target
    private Neuron target;
    private double weight;

    public Synapse(Neuron source, Neuron target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }
}
