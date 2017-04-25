/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.player.cache;


import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;

/**
 * Created by Jonah on 6/11/2015.
 */
public abstract class CachePlayer extends AutoMongo {

    public abstract String getName();

    public abstract String getUniqueId();

}
