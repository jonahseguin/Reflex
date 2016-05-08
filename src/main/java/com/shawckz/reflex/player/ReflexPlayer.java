/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.player;

import com.shawckz.reflex.cache.CachePlayer;
import com.shawckz.reflex.check.*;
import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.reflex.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.database.mongo.serial.MapSerializer;
import lombok.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@CollectionName(name = "aresplayers")
public class ReflexPlayer extends CachePlayer {

    @MongoColumn(name = "vl")
    @DatabaseSerializer(serializer = MapSerializer.class)
    @Getter
    private final Map<CheckType, Integer> violations = new HashMap<>();
    @Getter
    private final Map<String, Violation> vls = new HashMap<>();
    @Getter
    private final OldCheckData data = new OldCheckData();
    private final Map<CheckType, Checker> checkers = new HashMap<>();
    private final Set<CheckType> training = new HashSet<>();
    @Getter @Setter private Player bukkitPlayer = null;
    @MongoColumn(name = "username")
    @NonNull @Getter @Setter private String name;
    @MongoColumn(name = "uuid", identifier = true)
    @NonNull @Getter @Setter private String uniqueId;
    @MongoColumn(name = "totalVL")
    @Getter @Setter private int totalVL = 0;
    @Getter @Setter private boolean alertsEnabled = true;

    public ReflexPlayer() {//So that AutoMongo can instantiate without throwing an InstantiationException
    }

    public Violation addVL(CheckType checkType, boolean cancelled){
        Check check = CheckManager.get().getCheck(checkType);
        if (check == null) {
            throw new NullPointerException("Check can't be null");
        }
        totalVL += check.getRaiseLevel();
        if(!violations.containsKey(checkType)){
            violations.put(checkType,0);
        }
        violations.put(checkType,violations.get(checkType)+check.getRaiseLevel());

        final Violation violation = new Violation(this, this.getTotalVL(), checkType,
                this.getVL(checkType),cancelled, getChecker(checkType).cloneData());//Cloned checkdata/checker

        vls.put(violation.getId(), violation);

        return violation;
    }

    public int getVL(CheckType hackType){
        if(violations.containsKey(hackType)){
            return violations.get(hackType);
        }
        violations.put(hackType,0);
        return 0;
    }

    public Checker getChecker(CheckType checkType) {
        if(!checkers.containsKey(checkType)) {
            checkers.put(checkType, instantiateChecker(checkType));
        }
        return checkers.get(checkType);
    }

    private Checker instantiateChecker(CheckType checkType) {
        Checker checker;
        try{
            checker = checkType.getChecker().getConstructor(CheckType.class, ReflexPlayer.class).newInstance(checkType, this);
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException |InvocationTargetException ex) {
            throw new RuntimeException("Reflex could not instantiate CheckData: ", ex);
        }
        return checker;
    }

    public void msg(String msg) {
        bukkitPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void startTraining(CheckType checkType) {
        training.add(checkType);
    }

    public boolean isTraining(CheckType checkType) {
        return training.contains(checkType);
    }

    public void stopTraining(CheckType checkType) {
        training.remove(checkType);
    }
}
