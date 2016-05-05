package com.shawckz.reflex.cmd.commands;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.cmd.CmdArgs;
import com.shawckz.reflex.cmd.Command;
import com.shawckz.reflex.cmd.ReflexCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(name = "help", usage = "/ares help")
public class CommandHelp implements ReflexCommand {

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        CommandSender sender = cmdArgs.getSender();
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7*** [&c&lReflex&r&7] ***"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7v"+Reflex.getPlugin().getDescription().getVersion()+" by Shawckz"));
        for(ReflexCommand cmd : Reflex.getCommandHandler().getCommands()){
            if(cmd.getClass().isAnnotationPresent(Command.class)){
                Command c = cmd.getClass().getAnnotation(Command.class);
                sender.sendMessage(ChatColor.RED+"/reflex "+ChatColor.GRAY+c.name());
            }
        }
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
    }
}
