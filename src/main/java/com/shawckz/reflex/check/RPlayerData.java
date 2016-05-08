/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.check;

import lombok.*;

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
public class RPlayerData implements ReflexData {

    private final int trainingPeriod;//how many seconds this data was recorded across

    private int ping = -1;
    private int clicks = 0;//Clicks
    private int attacks = 0;//Attacks
    private int damage = 0;//Damage dealt
    private double yawRate = 0;//Yaw change per second (yawRate += (Math.abs(e.getFrom.getYaw()) - Math.abs(e.getTo().getYaw())
    private double accuracy = 0;// hits/miss %
    private int hits = 0;
    private int misses = 0;
    private int jumps = 0;//Jumps per second
    private int distanceMoved;//Blocks per second

    private long lastUpdateTime = 0;//Last time this data was updated


}
