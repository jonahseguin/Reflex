/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.neural;

import java.util.Random;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class NeuralCore {

    public static final Random RANDOM = new Random();

    public static double generateWeight() {
        return (-0.5) + (0.5 - (-0.5)) * RANDOM.nextDouble();
    }

}
