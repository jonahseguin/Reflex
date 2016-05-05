package com.shawckz.reflex.check.checker;

import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.reflex.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.player.ReflexPlayer;
import com.shawckz.reflex.util.ReflexException;
import com.shawckz.reflex.util.serial.ReflexPlayerSerializer;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Getter
@Setter
public abstract class Checker extends CheckerBackend {

    @MongoColumn(name = "check")
    private CheckType check;

    @MongoColumn(name = "_id", identifier = true)
    private String id;

    @MongoColumn(name = "player")
    @DatabaseSerializer(serializer = ReflexPlayerSerializer.class)
    private ReflexPlayer player;

    public Checker(CheckType check, ReflexPlayer player) {
        this.check = check;
        this.id = UUID.randomUUID().toString().toLowerCase();
        this.player = player;
    }

    public abstract CheckerSimilarity getSimilarityTo(Checker checker);

    public Checker cloneData() {
        Class c = this.getClass();
        List<Field> fields = new ArrayList<>();

        for(Field f : c.getDeclaredFields()){
            fields.add(f);
        }
        c = this.getClass().getSuperclass();
        for(Field f : c.getDeclaredFields()){
            fields.add(f);
        }

        Map<String, Object> values = new HashMap<>();

        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(CheckerData.class)) {
                CheckerData data = f.getAnnotation(CheckerData.class);
                try{
                    values.put(data.value(), f.get(this));
                }
                catch (IllegalAccessException ex) {
                    throw new ReflexException("Could not access field", ex);
                }
            }
        }

        try {
            Checker checker = (Checker) this.getClass().getSuperclass().getConstructor(CheckType.class, ReflexPlayer.class).newInstance(this.check, this.player);

            for(Field f : checker.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(CheckerData.class)) {
                    CheckerData data = f.getAnnotation(CheckerData.class);
                    if(values.containsKey(data.value())) {
                        f.set(this, values.get(data.value()));
                    }
                }
            }

            return checker;
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new ReflexException("Could not instantiate checker for cloning data", ex);
        }
    }

}
