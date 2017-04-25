/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.base;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigSerializer;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Superclass of all Checks, sorted into directories based on their RCheckType
 * Each individual oldchecks has it's own Config file
 * All fields annotated with @ConfigData will be automatically loaded to the fields from the respective configuration file upon #load being called.
 * And saved from the local fields to the config when #save is called.
 */
public class CheckConfig {

    private final File file;
    private final File directory;
    private YamlConfiguration config;
    private RCheckType rCheckType;
    private CheckType checkType;
    private Check check;

    public CheckConfig(CheckType checkType, RCheckType rCheckType) {
        String directory = Reflex.getInstance().getDataFolder().getPath();
        this.checkType = checkType;
        this.rCheckType = rCheckType;
        this.directory = new File(directory + File.separator + rCheckType.toString().toLowerCase());
        this.file = new File(this.directory, checkType.getName() + ".yml");
        this.config = new YamlConfiguration();
        createFile();
    }

    public void createFile() {
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config.load(file);
        }
        catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public void setupConfig() {
        save();
        load();
        saveConfig();
        if (rCheckType == RCheckType.INSPECT) {
            check = Reflex.getInstance().getInspectManager().getInspector(checkType);
        }
        else if (rCheckType == RCheckType.DATA) {
            check = Reflex.getInstance().getDataCaptureManager().getDataCapture(checkType);
        }
        else if (rCheckType == RCheckType.TRIGGER) {
            check = Reflex.getInstance().getTriggerManager().getTrigger(checkType);
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void save() {
        Class c = this.getClass();

        List<Field> fields = new ArrayList<>();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = this.getClass().getSuperclass();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = c.getSuperclass();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = c.getSuperclass();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }


        for (Field f : fields) {
            if (f.isAnnotationPresent(ConfigData.class)) {
                ConfigData configData = f.getAnnotation(ConfigData.class);
                try {
                    f.setAccessible(true);
                    Object saveValue = f.get(this);

                    if (f.isAnnotationPresent(ConfigSerializer.class)) {
                        ConfigSerializer serializer = f.getAnnotation(ConfigSerializer.class);
                        AbstractSerializer as = (AbstractSerializer) serializer.serializer().newInstance();
                        saveValue = as.toString(saveValue);
                    }
                    final String path = configData.value();
                    try {
                        config.addDefault(path, saveValue);
                        config.set(path, saveValue);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void load() {
        Class c = this.getClass();

        List<Field> fields = new ArrayList<>();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = this.getClass().getSuperclass();


        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }
        c = c.getSuperclass();


        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = c.getSuperclass();


        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }


        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(ConfigData.class)) {
                ConfigData configData = f.getAnnotation(ConfigData.class);
                final String path = configData.value();
                if (config.contains(path)) {
                    f.setAccessible(true);
                    if (!f.isAnnotationPresent(ConfigSerializer.class)) {
                        try {
                            if (f.getClass().isInstance(check)) {
                                f.set(check, config.get(path));
                            }
                            else {
                                f.set(this, config.get(path));
                            }
                        }
                        catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            AbstractSerializer serializer = (AbstractSerializer) f.getAnnotation(ConfigSerializer.class).serializer().newInstance();
                            f.set(this, serializer.fromString(config.get(path)));
                        }
                        catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public File getFile() {
        return file;
    }
}
