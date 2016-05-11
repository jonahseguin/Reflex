/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.inspect;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Jonah Seguin on 5/8/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
public abstract class Neuron {

    public static final Random RANDOM = new Random();

    private final String id = UUID.randomUUID().toString().substring(0,6);
    protected double accuracy = 0;
    protected RTrainer base;
    protected double minSimilarity;

    public Neuron(RTrainer base, double minSimilarity) {
        this.base = base;
        this.minSimilarity = Math.random();
        Bukkit.getLogger().info("Neuron created with minSimilarity of " + minSimilarity);
    }

    public Neuron(RTrainer base) {
        this(base, Math.random());
        Bukkit.getLogger().info("Neuron created with minSimilarity of (RANDOM) " + minSimilarity);
    }

    /**
     * Fire this neuron, update the accuracy
     * Calculate the accuracy - based on similarity between base data and input data
     * @param input
     * @return
     */
    public abstract NeuronResult fire(ReflexInput input);

}
