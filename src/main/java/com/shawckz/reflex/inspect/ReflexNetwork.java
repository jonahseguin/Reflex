/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.inspect;


import com.shawckz.reflex.util.ReflexException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jonah Seguin on 5/8/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class ReflexNetwork {

    private String id;

    public ReflexNetwork() {
        this.id = UUID.randomUUID().toString();
    }



    private final List<Neuron> neurons = new ArrayList<>();

    public void addNeuron(Neuron neuron) {
        neurons.add(neuron);
    }

    public void removeNeuron(Neuron neuron) {
        neurons.remove(neuron);
    }

    public RNetworkOutcome fire(ReflexInput input) {
        Neuron highest = null;
        NeuronResult result = null;
        for(Neuron neuron : neurons) {
            NeuronResult r = neuron.fire(input);
            if (highest == null) {
                highest = neuron;
                result = r;
            }
            else{
                if (r.getAccuracy() > result.getAccuracy() && neuron.getAccuracy() > neuron.getMinSimilarity()) {
                    highest = neuron;
                    result = r;
                }
            }
        }

        if(highest == null || result == null) {
            throw new ReflexException("Cannot fire network - not enough neurons (null result/highest)");
        }

        if(input instanceof RTrainer) {
            RTrainer trainer = (RTrainer) input;
            if(trainer.getExpectedOutput() != result.getOutput()) {
                Bukkit.broadcastMessage(ChatColor.AQUA + "Invalid expected output for final neuron - removing and rerunning network");
                Bukkit.broadcastMessage(ChatColor.GRAY + "(Expected " + trainer.getExpectedOutput().toString() + ", but got " + result.getOutput().toString()+")");
                Bukkit.broadcastMessage(ChatColor.GRAY + "With accuracy of " + result.getAccuracy());
                //The expected output was not the actual output: Remove the falsely trained neuron
                removeNeuron(highest);
                return fire(input);//Re-go through the network after the neuron has been removed
            }
        }


        return new RNetworkOutcome(highest, result);
    }

}
