/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.backend.configuration;

import org.bukkit.command.CommandSender;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public enum ReflexPerm {

    ALERTS("reflex.alerts"),
    BYPASS("reflex.bypass"),
    USE("reflex.use");

    private final String perm;

    ReflexPerm(String perm) {
        this.perm = perm;
    }

    public String getPerm() {
        return perm;
    }

    public boolean hasPerm(CommandSender player) {
        return player.hasPermission(getPerm());
    }

}
