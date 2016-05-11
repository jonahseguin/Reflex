/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.core.player;

import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.cache.CachePlayer;
import com.shawckz.reflex.core.database.mongo.annotations.CollectionName;
import com.shawckz.reflex.core.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.inspect.RInspectData;
import com.shawckz.reflex.inspect.RTester;
import com.shawckz.reflex.inspect.RTrainer;
import com.shawckz.reflex.prevent.check.CheckData;
import com.shawckz.reflex.util.ReflexException;
import lombok.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@CollectionName(name = "reflex_players")
@Getter
@Setter
public class ReflexPlayer extends CachePlayer {

    @MongoColumn(name = "username")
    @NonNull private String name;

    @MongoColumn(name = "uuid", identifier = true)
    @NonNull private String uniqueId;

    private int sessionVL = 0;

    @MongoColumn(name = "checkViolations")
    private Set<String> checkViolations = new HashSet<>();

    @MongoColumn(name = "vl")
    private Map<String, Integer> vl = new HashMap<>();


    /* Prevent / checks */
    private final CheckData data = new CheckData();

    /* Inspect & neural */
    private final Map<CheckType, RTrainer> training = new HashMap<>();
    private final Map<CheckType, RTester> testing = new HashMap<>();

    @Getter @Setter private Player bukkitPlayer = null;
    @Getter @Setter private boolean alertsEnabled = true;

    public ReflexPlayer() {//So that AutoMongo can instantiate without throwing an InstantiationException
    }


    public void msg(String msg) {
        bukkitPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public CheckData getData() {
        return data;
    }

    /* TRAINING */
    public void startTraining(CheckType checkType, RTrainer trainer) {
        training.put(checkType, trainer);
        trainer.start();
    }

    public boolean isTraining(CheckType checkType) {
        return training.containsKey(checkType);
    }

    public RTrainer stopTraining(CheckType checkType) {
        if(isTraining(checkType)) {
            RTrainer trainer = training.get(checkType);
            training.remove(checkType);
            trainer.cancel();
            return trainer;
        }
        return null;
    }

    public RTrainer getTrainer(CheckType checkType) {
        return training.get(checkType);
    }

    public RInspectData getTrainerPlayerData(CheckType checkType) {
        RTrainer trainer = getTrainer(checkType);
        if(trainer != null) {
            return trainer.getData();
        }
        return null;
    }

    /* TESTING */

    public void startTesting(CheckType checkType, RTester tester) {
        testing.put(checkType, tester);
        tester.start();
    }

    public boolean isTesting(CheckType checkType) {
        return testing.containsKey(checkType);
    }

    public RTester stopTesting(CheckType checkType) {
        if(isTesting(checkType)) {
            RTester tester = testing.get(checkType);
            testing.remove(checkType);
            tester.cancel();
            return tester;
        }
        return null;
    }

    public RTester getTester(CheckType checkType) {
        return testing.get(checkType);
    }

    public RInspectData getTesterPlayerData(CheckType checkType) {
        RTester tester = getTester(checkType);
        if(tester != null) {
            return tester.getData();
        }
        return null;
    }

    public boolean shouldCaptureData(CheckType checkType) {
        return isTraining(checkType) || isTesting(checkType);
    }

    public RInspectData getInputData(CheckType checkType) {
        if(isTraining(checkType) && isTesting(checkType)) {
            throw new ReflexException("Player is training and testing at same time");
        }
        else{
            if(isTraining(checkType)) {
                return getTrainerPlayerData(checkType);
            }
            else if (isTesting(checkType)) {
                return getTesterPlayerData(checkType);
            }
            else{
                throw new ReflexException("Player is not training or testing");
            }
        }
    }

    public void addVL(CheckType checkType) {
        int vl = getVL(checkType);
        this.vl.put(checkType.getName(), (vl + 1));
    }

    public int getVL(CheckType checkType) {
        if(!vl.containsKey(checkType.getName())) {
            vl.put(checkType.getName(), 0);
        }
        return vl.get(checkType.getName());
    }

}
