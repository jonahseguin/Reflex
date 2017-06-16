package com.jonahseguin.reflex.commands;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.command.RCmd;
import com.jonahseguin.reflex.backend.command.RCmdArgs;
import com.jonahseguin.reflex.backend.command.RCommand;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Jonah on 6/16/2017.
 */
public class CmdDebug implements RCommand {

    @RCmd(name = "debug", usage = "/reflex debug", description = "Toggle debug mode", permission = ReflexPerm.DEBUG)
    public void onCmdDebug(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        Reflex.getInstance().getReflexConfig().setDebug(!isDebug());
        sender.sendMessage(ChatColor.GRAY + "Debug mode toggled, enabled:" + isDebug());
        ReflexLang.DEBUG_TOGGLE.sendToStaff((isDebug() ? "&aenabled" : "&cdisabled"));

    }

    public boolean isDebug() {
        return Reflex.getInstance().getReflexConfig().isDebug();
    }

}
