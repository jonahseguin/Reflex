/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.inspect;

/**
 * Created by Jonah Seguin on 5/8/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public enum NeuronOutput {

    HACKER,
    LEGIT;

    public NeuronOutput opposite() {
        if(this == HACKER) {
            return LEGIT;
        }
        else{
            return HACKER;
        }
    }

}
