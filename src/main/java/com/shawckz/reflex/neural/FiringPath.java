/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.neural;

import com.shawckz.reflex.util.ReflexException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FiringPath {

    //Path (order) at which neurons fired

    private List<Neuron> path = new ArrayList<>();

    public FiringPath(List<Neuron> path) {
        this.path = path;
    }

    public FiringPath() {
    }

    public void add(Neuron neuron) {
        path.add(neuron);
    }

    public Neuron getFirst() {
        if(path.isEmpty()) throw new ReflexException("No neurons in path");
        return path.get(0);
    }

    public Neuron getLast() {
        if(path.isEmpty()) throw new ReflexException("No neurons in path");
        return path.get(path.size());
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

    public Iterator<Neuron> iterator() {
        return path.iterator();
    }

}
