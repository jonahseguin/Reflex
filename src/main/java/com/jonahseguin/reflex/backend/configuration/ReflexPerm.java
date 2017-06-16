/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.backend.configuration;

import org.bukkit.command.CommandSender;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public enum ReflexPerm {

    NONE(""), //Signifies a non-permission, do not touch
    STAFF("reflex.staff"), // Denotes a staff member, if a player has this permission
    ALERTS("reflex.alerts"),
    BYPASS("reflex.bypass"),
    USE("reflex.use"),
    LOOKUP_PLAYER("reflex.lookup.player"),
    LOOKUP_BAN("reflex.lookup.ban"),
    UNBAN("reflex.unban"),
    CONFIRM_BAN("reflex.confirmban"),
    SETTINGS("reflex.settings"),
    CONFIG_LOAD("reflex.config.load"),
    CONFIG_SET("reflex.config.set"),
    CONFIG_RESET("reflex.config.reset"),
    CANCEL("reflex.cancel"),
    CANCEL_ALL("reflex.cancel.all"),
    CHECKS("reflex.checks"),
    LOOKUP_ALERT("reflex.lookup.alert"),
    NOTE_ADD("reflex.note.add"),
    NOTE_VIEW("reflex.note.view"),
    DEBUG("reflex.debug"),
    CHECK("reflex.check");

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
