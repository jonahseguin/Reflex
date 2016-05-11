/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.inspect;

import com.shawckz.reflex.util.ReflexException;
import lombok.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Jonah Seguin on 5/8/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class RInspectData {

    private final int trainingPeriod;//how many seconds this data was recorded across

    private int ping = -1;
    @DataConvert private int clicks = 0;//Clicks
    @DataConvert private double damageDealt = 0;//Damage dealt
    @DataConvert private double damageTaken = 0;//Damage taken
    @DataConvert private double yawRate = 0;//Yaw change per second (yawRate += (Math.abs(e.getFrom.getYaw()) - Math.abs(e.getTo().getYaw())
    private double accuracy = 0;// hits/miss %
    @DataConvert private int hits = 0;//Hits - amount of times this player has hit another player
    @DataConvert private int hitsTaken = 0;//Hits taken (amount of times this player has gotten hti)
    private int misses = 0;
    @DataConvert private int jumps = 0;//Jumps
    @DataConvert private double distanceMoved;//Blocks moved

    public void finish() {
        this.accuracy = hits  / (misses <= 0 ? 1 : misses);
        this.normal = getData();
    }

    private Map<Field, Object> normal = null;
    private Map<Field, Object> perSecond = null;

    private void load(Map<Field, Object> data) {
        try {
            for (Field f : data.keySet()) {
                Field lf = this.getClass().getDeclaredField(f.getName());
                lf.setAccessible(true);
                lf.set(this, data.get(f));
            }
        }
        catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new ReflexException("could not load data into rplayerdata", ex);
        }
    }

    public Map<Field, Object> getData() {
        Map<Field, Object> data = new HashMap<>();

        try {
            for (Field f : this.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                data.put(f, f.get(this));
            }
        }
        catch (IllegalAccessException ex) {
            throw new ReflexException("could not fetch data from rplayerdata", ex);
        }

        return data;
    }

    public void convertToPerSecond() {
        if(normal == null) {
            normal = getData();
        }
        Map<Field, Object> data = getData();
        for(Field f : data.keySet()) {
            Object o = data.get(f);
            if(f.isAnnotationPresent(DataConvert.class)) {
                if(o instanceof Double) {
                    Double val = (Double) o;
                    data.put(f, val);
                }
                else if (o instanceof Integer) {
                    Integer val = (Integer) o;
                    data.put(f, val);
                }
                else if (o instanceof Long) {
                    Long val = (Long) o;
                    data.put(f, val);
                }
                else if (o instanceof Float) {
                    Float val = (Float) o;
                    data.put(f, val);
                }
            }
        }
        this.perSecond = data;
        load(perSecond);
    }

    public void convertToNormal() {
        if(normal == null) {
            normal = getData();
        }
        load(normal);
    }

    public double getSimilarityTo(RInspectData data) {
        Map<Field, Object> thisData = getData();
        Map<Field, Object> thatData = data.getData();

        List<Double> similarity = new ArrayList<>();

        for(Field f : thisData.keySet()) {
            loop:
            for(Field tf : thatData.keySet()) {
                if(tf.getName().equalsIgnoreCase(f.getName())) {
                    Object o1 = thisData.get(f);
                    Object o2 = thatData.get(tf);

                    if(o1 instanceof Double) {
                        Double d1 = (Double) o1;
                        Double d2 = (Double) o2;
                        similarity.add(similarity(d1, d2));
                    }
                    else if (o1 instanceof Integer) {
                        Integer d1 = (Integer) o1;
                        Integer d2 = (Integer) o2;
                        similarity.add(similarity(d1, d2));
                    }
                    else if (o1 instanceof Long) {
                        Long d1 = (Long) o1;
                        Long d2 = (Long) o2;
                        similarity.add(similarity(d1, d2));
                    }
                    else if (o1 instanceof Float) {
                        Float d1 = (Float) o1;
                        Float d2 = (Float) o2;
                        similarity.add(similarity(d1, d2));
                    }
                    break loop;
                }
            }
        }

        return meanAvg(similarity);
    }

    private double meanAvg(Collection<Double> a) {
        double x = 0;
        for(double d : a) {
            x += d;
        }
        return (x / a.size());
    }

    private double similarity(double a, double b) {
        double x = Math.max(a, b);
        double z = Math.min(a, b);
        if(x == 0) {
            x = 1;
        }
        return z / x;
    }

}
