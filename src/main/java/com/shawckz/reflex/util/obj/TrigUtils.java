/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.obj;

import org.bukkit.Location;

public class TrigUtils {

    public static double getDirection(Location from, Location to) {
        if ((from == null) || (to == null)) {
            return 0.0D;
        }
        double difX = to.getX() - from.getX();
        double difZ = to.getZ() - from.getZ();

        return wrapAngleTo180((float) (Math.atan2(difZ, difX) * 180.0D / 3.141592653589793D) - 90.0F);
    }

    public static double getDistance(double p1, double p2, double p3, double p4) {
        double delta1 = p3 - p1;
        double delta2 = p4 - p2;

        return Math.sqrt(delta1 * delta1 + delta2 * delta2); //a2 + b2 = c2
    }

    public static float wrapAngleTo180(float value) {
        value %= 360.0F;
        if (value >= 180.0F) {
            value -= 360.0F;
        }
        if (value < -180.0F) {
            value += 360.0F;
        }
        return value;
    }

    public static float fixRotation(float angle, float min, float max) {
        float w = wrapAngleTo180(min - angle);
        if (w > max) {
            w = max;
        }
        if (w < -max) {
            w = -max;
        }
        return angle + w;
    }

}
