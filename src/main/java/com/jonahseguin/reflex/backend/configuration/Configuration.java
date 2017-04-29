/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.backend.configuration;


import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigSerializer;
import com.jonahseguin.reflex.util.obj.RReflecUtil;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;


@Getter
public class Configuration {

    private final YamlConfiguration config;
    private final File file;
    private final File directory;

    public Configuration(Plugin plugin) {
        this(plugin, "config.yml");
    }

    public Configuration(Plugin plugin, String filename) {
        this(filename, plugin.getDataFolder().getPath());
    }

    public Configuration(String filename, String directory) {
        this.directory = new File(directory);
        this.file = new File(directory, filename);
        config = new YamlConfiguration();
        createFile();
    }

    public void createFile() {
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        Field[] toSave = this.getClass().getDeclaredFields();
        for (Field f : toSave) {
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
                    config.addDefault(configData.value(), saveValue);
                    config.set(configData.value(), saveValue);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setValue(String key, String value) {
        Field[] toLoad = this.getClass().getDeclaredFields();
        for (Field f : toLoad) {
            if (f.isAnnotationPresent(ConfigData.class)) {
                ConfigData configData = f.getAnnotation(ConfigData.class);
                f.setAccessible(true);
                if (configData.value().equalsIgnoreCase(key)) {
                    Object val = value;
                    if (!f.isAnnotationPresent(ConfigSerializer.class)) {
                        val = RReflecUtil.toObject(f.getType(), value);
                        try {
                            f.set(this, val);
                        } catch (IllegalAccessException expected) {
                            return false;
                        }
                    } else {
                        try {
                            AbstractSerializer serializer = (AbstractSerializer) f.getAnnotation(ConfigSerializer.class).serializer().newInstance();
                            f.set(this, serializer.fromString(value));
                        } catch (InstantiationException expected) {
                            return false;
                        } catch (IllegalAccessException expected) {
                            return false;
                        } catch (Exception expected) {
                            return false;
                        }
                    }
                }
            }
        }
        save();
        return true;
    }

    public void load() {
        Field[] toLoad = this.getClass().getDeclaredFields();
        for (Field f : toLoad) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(ConfigData.class)) {
                ConfigData configData = f.getAnnotation(ConfigData.class);
                if (config.contains(configData.value())) {
                    f.setAccessible(true);
                    if (!f.isAnnotationPresent(ConfigSerializer.class)) {
                        try {
                            f.set(this, config.get(configData.value()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            AbstractSerializer serializer = (AbstractSerializer) f.getAnnotation(ConfigSerializer.class).serializer().newInstance();
                            f.set(this, serializer.fromString(config.get(configData.value())));
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
