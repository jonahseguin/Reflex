/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.commands;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.command.RCmd;
import com.jonahseguin.reflex.backend.command.RCmdArgs;
import com.jonahseguin.reflex.backend.command.RCommand;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.ban.AutobanManager;
import com.jonahseguin.reflex.check.alert.AlertManager;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCancel implements RCommand {

    @RCmd(name = "reflex cancel", usage = "/cancel <player>", minArgs = 1, permission = ReflexPerm.CANCEL, aliases = {"! cancel", "reflex cancel", "rx cancel", "rflex cancel", "cancel"}, description = "Cancel an auto-ban on a player")
    public void onCmdCancel(RCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();

        String target = args.getArg(0);
        {
            Player t = Bukkit.getPlayer(target);
            if (t != null) {
                target = t.getName();
            }
        }

        AutobanManager autobanManager = Reflex.getInstance().getAutobanManager();

        if (autobanManager.hasAutoban(target)) {
            autobanManager.removeAutoban(target);
            AlertManager.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.CANCEL, target, sender.getName()));
        } else {
            RLang.send(sender, ReflexLang.CANCEL_NOT_BEING_BANNED);
        }

    }

    @RCmd(name = "reflex cancelall", usage = "/reflex cancelall", permission = ReflexPerm.CANCEL_ALL,
            aliases = {"! cancelall", "rx cancelall", "rflex cancelall", "cancelall"}, description = "Cancel all active auto-bans")
    public void onCmdCancelAll(RCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();

        AutobanManager autobanManager = Reflex.getInstance().getAutobanManager();
        Iterator<String> it = autobanManager.getAutobans().keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String key = it.next();
            autobanManager.removeAutoban(key);
            count++;
        }

        AlertManager.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.CANCEL_ALL, count + "", sender.getName()));

    }

}
