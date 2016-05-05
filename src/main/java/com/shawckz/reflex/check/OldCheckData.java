package com.shawckz.reflex.check;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OldCheckData {

    /**
     * Speed
     */
    private double blocksPerSecond = 0;

    /**
     * NoSwing
     */
    private boolean hasSwung = false;

    /**
     * BedFly
     */
    private boolean enteredBed = false;

    /**
     * FastBow
     */
    private long bowPull = 0;
    private long bowShoot = 0;
    private double bowPower = 0;

    /**
     * Regen
     */
    private double healthPerSecond = 0;

    /**
     * HighJump
     */
    private boolean jumping;
}
