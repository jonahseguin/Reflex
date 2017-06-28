/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.backend.database;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.Configuration;
import com.jonahseguin.reflex.backend.configuration.annotations.ConfigData;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;


/**
 * The DBManager class
 * Used to handle the connection the MongoClient.
 * The setupConfig method should not be touched except in the PureCore onEnable
 */
public class DBManager extends Configuration {

    private static MongoClient mongoClient;
    private static MongoDatabase db;


    @ConfigData("database.name")
    private static String databaseName = "xxx";
    @ConfigData("database.authName")
    private static String authDatabaseName = "xxx";
    @ConfigData("database.host")
    private static String host = "xxx";
    @ConfigData("database.port")
    private static int port = 3309;
    @ConfigData("database.credentials.username")
    private static String username = "xxx";
    @ConfigData("database.credentials.password")
    private static String password = "xxx";
    @ConfigData("database.useAuth")
    private static boolean useAuth = false;


    public DBManager(Plugin plugin) {
        super(plugin, "database.yml");
        load();
        save();
        setup();
    }

    public static MongoDatabase getDb() {
        return db;
    }

    public static void setDb(MongoDatabase db) {
        DBManager.db = db;
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static void setMongoClient(MongoClient mongoClient) {
        DBManager.mongoClient = mongoClient;
    }

    private void setup() {
        if (host.equals("xxx")) {
            Reflex.getReflexLogger().info(" ");
            Reflex.getReflexLogger().info("-------------------------------------");
            Reflex.getReflexLogger().info("Reflex - v" + Reflex.getInstance().getDescription().getVersion() + " by Jonah Seguin (Shawckz)");
            Reflex.getReflexLogger().info("https://shawckz.com/product/Reflex");
            Reflex.getReflexLogger().info(" ");
            Reflex.getReflexLogger().info("Reflex was unable to start because your MongoDB database.yml has not been configured.");
            Reflex.getReflexLogger().info("Please stop the server, edit the plugins/Reflex/database.yml to match your MongoDB information, and start the server.");
            Reflex.getReflexLogger().info("For more help on setting up MongoDB with Reflex, visit https://shawckz.com/help#reflex-mongo");
            Reflex.getReflexLogger().info("Reflex will now disable...");
            Reflex.getReflexLogger().info("-------------------------------------");
            Reflex.getReflexLogger().info(" ");
            Bukkit.getServer().getPluginManager().disablePlugin(Reflex.getInstance());
            return;
        }
        try {
            if (!useAuth) {
                mongoClient = new MongoClient(new ServerAddress(host, port));
            } else {
                MongoCredential credential = MongoCredential.createCredential(username, authDatabaseName, password.toCharArray());
                mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));
            }
            db = mongoClient.getDatabase(databaseName);
        } catch (MongoException ex) {
            Reflex.getReflexLogger().info(" ");
            Reflex.getReflexLogger().info("-------------------------------------");
            Reflex.getReflexLogger().info("Reflex - v" + Reflex.getInstance().getDescription().getVersion() + " by Jonah Seguin (Shawckz)");
            Reflex.getReflexLogger().info("https://shawckz.com/product/Reflex");
            Reflex.getReflexLogger().info(" ");
            Reflex.getReflexLogger().info("Reflex was unable to start because MongoDB was unable to connect.");
            Reflex.getReflexLogger().info("Ensure that your MongoDB server is properly configured, and that your database.yml matches those settings.");
            Reflex.getReflexLogger().info("For more help on setting up MongoDB with Reflex, visit https://shawckz.com/help#reflex-mongo");
            Reflex.getReflexLogger().info("[ERROR]: " + ex.getMessage());
            Reflex.getReflexLogger().info("Reflex will now disable...");
            Reflex.getReflexLogger().info("-------------------------------------");
            Reflex.getReflexLogger().info(" ");
            Bukkit.getServer().getPluginManager().disablePlugin(Reflex.getInstance());
        }
    }
}
