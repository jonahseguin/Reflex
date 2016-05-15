/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.util.obj;

import org.bukkit.Location;

/**
 * Created by Jonah on 6/27/2015.
 */
public class Distance {

    private final double l1Y;
    private final double l2Y;

    private final double XDiff;
    private final double YDiff;
    private final double ZDiff;

    public Distance(Location from, Location to) {
        l1Y = to.getY();
        l2Y = from.getY();

        XDiff = Math.abs(to.getX() - from.getX());
        ZDiff = Math.abs(to.getZ() - from.getZ());
        YDiff = Math.abs(l1Y - l2Y);
    }

    public double fromY() {
        return l2Y;
    }

    public double toY() {
        return l1Y;
    }

    public double getXDifference() {
        return XDiff;
    }

    public double getZDifference() {
        return ZDiff;
    }

    public double getYDifference() {
        return YDiff;
    }

}
