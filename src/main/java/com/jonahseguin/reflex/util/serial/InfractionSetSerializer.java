/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

import com.google.common.collect.Sets;
import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.check.violation.Infraction;
import com.jonahseguin.reflex.util.utility.ReflexException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jonah Seguin on Thu 2017-05-25 at 20:03.
 * Project: Reflex
 */
public class InfractionSetSerializer extends AbstractSerializer<Set<Infraction>> {

    private final InfractionSerializer serializer = new InfractionSerializer();

    @Override
    public String toString(Set<Infraction> data) {
        Set<String> d = new HashSet<>();
        for (Infraction infraction : data) {
            d.add(serializer.toString(infraction));
        }
        StringBuilder s = new StringBuilder();
        for (String x : d) {
            s.append(x).append(",");
        }
        return s.toString();
    }

    @Override
    public Set<Infraction> fromString(Object data) {
        if (data instanceof String) {
            String s = (String) data;
            Set<String> d = Sets.newHashSet(s.split(","));
            Set<Infraction> infractions = new HashSet<>();
            for (String x : d) {
                Infraction infraction = serializer.fromString(x);
                if (infraction != null) {
                    infractions.add(infraction);
                }
            }
            return infractions;
        } else {
            throw new ReflexException("Could not deserialize infraction; data is not String");
        }
    }
}
