package com.shawckz.reflex.prevent.check;


import com.shawckz.reflex.core.database.mongo.AutoMongo;
import com.shawckz.reflex.core.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.core.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.util.ReflexException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@CollectionName(name = "reflex_checkdata")
public class CheckData extends AutoMongo {

    @MongoColumn(name = "_id", identifier = true)
    private String id = UUID.randomUUID().toString();

    /**
     * AutoClick
     */
    @MongoColumn(name = "clicksPerSecond")
    private double[] clicksPerSecond = {0,0,0,0};

    /**
     * Speed
     */
    @MongoColumn(name = "blocksPerSecond")
    private double blocksPerSecond = 0;

    /**
     * NoSwing
     */
    @MongoColumn(name = "hasSwung")
    private boolean hasSwung = false;

    /**
     * BedFly
     */
    @MongoColumn(name = "enteredBed")
    private boolean enteredBed = false;

    /**
     * FastBow
     */
    @MongoColumn(name = "bowPull")
    private long bowPull = 0;
    @MongoColumn(name = "bowShoot")
    private long bowShoot = 0;
    @MongoColumn(name = "bowPower")
    private double bowPower = 0;

    /**
     * Regen
     */
    @MongoColumn(name = "healthPerSecond")
    private double healthPerSecond = 0;

    /**
     * HighJump
     */
    @MongoColumn(name = "jumping")
    private boolean jumping = false;

    public CheckData copy() {
        CheckData checkData = new CheckData();
        checkData.load(getData());
        return checkData;
    }

    private void load(Map<Field, Object> data) {
        try {
            for (Field f : data.keySet()) {
                Field lf = this.getClass().getDeclaredField(f.getName());
                lf.setAccessible(true);
                lf.set(this, data.get(f));
            }
        }
        catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new ReflexException("could not load data into checkdata", ex);
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
            throw new ReflexException("could not fetch data from checkdata", ex);
        }

        return data;
    }

}
