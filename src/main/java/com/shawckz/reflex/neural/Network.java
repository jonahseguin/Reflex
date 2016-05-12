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
public class Network {

    private List<Layer> layers = new ArrayList<>();

    private void testLayers() {
        Layer input = new Layer(Layer.Type.INPUT);

        Layer hidden = new Layer(Layer.Type.HIDDEN);

        Layer output = new Layer(Layer.Type.OUTPUT);



    }

    void test() {
        testLayers();


        FiringPath firingPath = new FiringPath();

        for(Layer layer : layers) {
            if(layer.getType() == Layer.Type.INPUT) {
                //Fire inputs
                for(Neuron neuron : layer.getNeurons()) {
                    neuron.fire(firingPath);
                }
            }
        }

        for(Layer layer : layers) {
            if(layer.getType() == Layer.Type.OUTPUT) {
                //Outputs
                for(Neuron neuron : layer.getNeurons()) {

                }
            }
        }

    }

}
