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
public class Neuron {

    private double weight;
    private double threshhold;
    private boolean fired = false;//true if weight >= threshhold
    private List<Synapse> synapses = new ArrayList<>();
    private double error = 0;
    private double output = 0;

    public Neuron(double threshhold) {
        this.threshhold = threshhold;
    }

    public Synapse connect(Neuron neuron) {
        Synapse synapse = new Synapse(this, neuron, NeuralCore.generateWeight());
        synapses.add(synapse);
        return synapse;
    }

    public double fire() {
        double sumWeights = 0;
        for (Synapse synapse : synapses) {
            sumWeights += (synapse.getTarget().isFired() ? synapse.getWeight() * synapse.getSource().getOutput() : 0);
        }

        this.fired = sumWeights > threshhold;
        return sumWeights;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isFired() {
        return fired;
    }

    public double getThreshhold() {
        return threshhold;
    }

    public void setThreshhold(double threshhold) {
        this.threshhold = threshhold;
    }

    public List<Synapse> getSynapses() {
        return synapses;
    }

    public double getError() {
        return error;
    }

    public double getOutput() {
        return output;
    }
}
