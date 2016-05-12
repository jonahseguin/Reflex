/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.neural;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class Layer {

    enum Type {
        INPUT,
        HIDDEN,
        OUTPUT
    }

    private List<Neuron> neurons = new ArrayList<>();
    private Type type;

    public Layer(Type type) {
        this.type = type;
    }

    public void addNeuron(Neuron neuron) {
        neurons.add(neuron);
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public Type getType() {
        return type;
    }
}
