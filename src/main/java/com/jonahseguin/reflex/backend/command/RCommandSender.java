/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.backend.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 12/13/2015.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class RCommandSender {

    private final CommandSender sender;

    public RCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    public CommandSender getCommandSender() {
        return sender;
    }

    public Player getPlayer() throws RCommandException {
        if (isPlayer()) return (Player) sender;
        throw new RCommandException("CommandSender is not a player");
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

}
