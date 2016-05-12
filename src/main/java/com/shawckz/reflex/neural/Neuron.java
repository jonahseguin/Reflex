/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.neural;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
@Setter
public class Neuron {

    private double weight;
    private double threshhold;
    private boolean fired = false;
    private List<Synapse> synapses = new ArrayList<>();
    private double error = 0;
    private NeuronOutput output = null;

    public Neuron(double threshhold) {
        this.threshhold = threshhold;
    }

    public Synapse connect(Neuron neuron) {
        Synapse synapse = new Synapse(this, neuron, NeuralCore.generateWeight());
        synapses.add(synapse);
        return synapse;
    }

    private double calculateOutput(double weight, double output) {
        return NeuralCore.sigmoid(weight) * output;
    }

    public NeuronOutput fire(FiringPath path) {
        double sumWeights = 0;
        for (Synapse synapse : synapses) {
            NeuronOutput out = synapse.getTarget().fire(path);
            if(out.isFired()) {
                path.add(synapse.getTarget());
                sumWeights += calculateOutput(out.getWeight(), out.getOutput());
            }
        }
        this.fired = sumWeights > threshhold;

        this.output = createOutput(sumWeights, fired, sumWeights);
        return output;
    }

    public NeuronOutput createOutput(double weight, boolean fired, double output) {
        return new NeuronOutput(weight, fired, output);
    }

}
