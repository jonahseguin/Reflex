package com.shawckz.reflex.cmd;

import com.shawckz.reflex.cmd.commands.CommandAlerts;
import com.shawckz.reflex.cmd.commands.CommandHelp;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by 360 on 5/30/2015.
 */

/**
 * The CommandHandler object
 * Used to register a set of commands and handle them within a JavaPlugin
 * ***Requires adding the commands to your plugin.yml***
 */
public class CommandHandler implements CommandExecutor {

    @Getter private List<ReflexCommand> commands;
    private JavaPlugin javaPlugin;

    public CommandHandler(JavaPlugin javaPlugin) {
        this.commands = new ArrayList<>();
        this.javaPlugin = javaPlugin;

        javaPlugin.getCommand("ares").setExecutor(this);

        registerCommand(new CommandHelp(), false);
        registerCommand(new CommandAlerts(), true);

    }

    private void registerCommand(ReflexCommand cmd, boolean single){
        if(!commands.contains(cmd)) {
            commands.add(cmd);
            if(single){
                if (cmd.getClass().isAnnotationPresent(Command.class)) {
                    Command command = cmd.getClass().getAnnotation(Command.class);
                    javaPlugin.getCommand(command.name()).setExecutor(this);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
        if(cmd.getName().equalsIgnoreCase("ares")){
            if(args.length > 0){
                String q = args[0];
                for(ReflexCommand c : commands){
                    if(c.getClass().isAnnotationPresent(Command.class)) {
                        Command cc = c.getClass().getAnnotation(Command.class);
                        if(cc.name().equalsIgnoreCase(q)){
                            String[] newArgs = new String[args.length - 1];
                            int x = 0;
                            for(int i = 1; i < args.length; i++){
                                newArgs[x] = args[i];
                                x++;
                            }
                            args = newArgs;
                            if (!sender.hasPermission("ares.use")) {
                                sender.sendMessage(cc.noPerm());
                                return true;
                            }
                            if (!sender.hasPermission(cc.permission()) && !cc.permission().equals("")) {
                                sender.sendMessage(cc.noPerm());
                                return true;
                            }
                            if (args.length < cc.minArgs()) {
                                sender.sendMessage(ChatColor.RED + "Usage: " + cc.usage());
                                return true;
                            }
                            if (cc.playerOnly() && !(sender instanceof Player)) {
                                sender.sendMessage(ChatColor.RED + "This is a player only command.");
                                return true;
                            }
                            c.onCommand(new CmdArgs(sender,args));
                            return true;
                        }
                    }
                }
            }
            else{
                for(ReflexCommand c : commands){
                    if(c.getClass().isAnnotationPresent(Command.class)) {
                        Command cc = c.getClass().getAnnotation(Command.class);
                        if(cc.name().equalsIgnoreCase("help")){
                            c.onCommand(new CmdArgs(sender,args));
                            return true;
                        }
                    }
                }
            }
        }
        for(ReflexCommand pCmd : commands){
            if(pCmd.getClass().isAnnotationPresent(Command.class)){
                Command command = pCmd.getClass().getAnnotation(Command.class);
                if(command.name().equalsIgnoreCase(cmd.getName())) {
                    if (!sender.hasPermission("ares.use")) {
                        sender.sendMessage(command.noPerm());
                        return true;
                    }
                    if (!sender.hasPermission(command.permission()) && !command.permission().equals("")) {
                        sender.sendMessage(command.noPerm());
                        return true;
                    }
                    if (args.length < command.minArgs()) {
                        sender.sendMessage(ChatColor.RED + "Usage: " + command.usage());
                        return true;
                    }
                    if (command.playerOnly() && !(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "This is a player only command.");
                        return true;
                    }

                    pCmd.onCommand(new CmdArgs(sender, args));
                    return true;
                }
            }
        }
        return true;
    }
}
