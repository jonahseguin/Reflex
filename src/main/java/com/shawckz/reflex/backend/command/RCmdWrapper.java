/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.backend.command;

/**
 * Created by Jonah Seguin on 12/13/2015.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class RCmdWrapper {

    private final RCommand command;
    private final RCommandCaller caller;
    private final String name;
    private final String[] aliases;
    private final String usage;
    private final String description;
    private final String permission;
    private final boolean playerOnly;
    private final int minArgs;

    public RCmdWrapper(RCommand command, RCommandCaller caller, String name, String[] aliases, String usage, String description, String permission, boolean playerOnly, int minArgs) {
        this.command = command;
        this.caller = caller;
        this.name = name;
        this.aliases = aliases;
        this.usage = usage;
        this.description = description;
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.minArgs = minArgs;
    }

    public void execute(RCmdArgs args) {
        caller.call(args);
    }

    public RCommand getCommand() {
        return command;
    }

    public RCommandCaller getCaller() {
        return caller;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public int getMinArgs() {
        return minArgs;
    }
}
