package com.shawckz.reflex.cache;


/*
 * Copyright (c) 2015 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

import com.shawckz.reflex.database.mongo.AutoMongo;

/**
 * Created by Jonah on 6/11/2015.
 */
public abstract class CachePlayer extends AutoMongo {

    public abstract String getName();

    public abstract String getUniqueId();

}
