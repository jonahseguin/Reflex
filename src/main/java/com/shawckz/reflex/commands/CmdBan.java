/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.commands;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.command.RCmd;
import com.shawckz.reflex.backend.command.RCmdArgs;
import com.shawckz.reflex.backend.command.RCommand;
import com.shawckz.reflex.backend.configuration.RLang;
import com.shawckz.reflex.backend.configuration.ReflexLang;
import com.shawckz.reflex.ban.ReflexBan;
import com.shawckz.reflex.event.ReflexConfirmBanEvent;
import com.shawckz.reflex.event.ReflexUnbanEvent;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Alert;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdBan implements RCommand {

    @RCmd(name = "reflex unban", usage = "/reflex unban <player>", permission = "reflex.unban", description = "Unban a player that was banned by Reflex",
            aliases = {"! unban", "reflex unban", "rx unban", "rflex unban"}, minArgs = 1)
    public void onCmdUnban(final RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        final String target = args.getArg(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(target);
                if (rp != null) {
                    if (Reflex.getInstance().getBanManager().hasBan(rp.getUniqueId())) {
                        ReflexBan ban = Reflex.getInstance().getBanManager().getBan(rp.getUniqueId());

                        ReflexUnbanEvent event = new ReflexUnbanEvent(rp, ban);
                        Bukkit.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            ban.setExpiration(0);
                            ban.setBanned(false);
                            ban.update();
                            Alert.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.UNBANNED, rp.getName(), sender.getName()));
                            sender.sendMessage(ChatColor.GRAY + "Unbanned " + rp.getName() + ".");
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Player '" + rp.getName() + "' has not been banned by Reflex.");
                    }
                }
                else {
                    RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND_DATABASE, target);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    @RCmd(name = "reflex confirmban", usage = "/reflex confirmban <player> <banned correctly(true/false)>", permission = "reflex.confirmban", description = "Confirm the result of a Reflex-ban",
            aliases = {"! confirmban", "reflex confirmban", "rx confirmban", "rflex confirmban"}, minArgs = 2)
    public void onCmdConfirmBan(final RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        final String target = args.getArg(0);

        final boolean confirm;
        try {
            confirm = Boolean.parseBoolean(args.getArg(1));
        }
        catch (Exception expected) {
            sender.sendMessage(ChatColor.RED + "The confirmed result must be a boolean (true/false)");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ReflexPlayer player = Reflex.getInstance().getCache().getReflexPlayer(target);
                if (player != null) {
                    if (Reflex.getInstance().getBanManager().hasBan(player.getUniqueId())) {
                        ReflexBan ban = Reflex.getInstance().getBanManager().getBan(player.getUniqueId());

                        ReflexConfirmBanEvent event = new ReflexConfirmBanEvent(ban, confirm);
                        Bukkit.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            ban.setConfirmed(true);
                            ban.setBannedCorrectly(confirm);
                            ban.update();
                            Alert.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.CONFIRM_BAN, player.getName(), sender.getName(), (confirm ? "&2correct" : "&4false")));
                            sender.sendMessage(ChatColor.GRAY + "Confirmed ban on " + player.getName() + " as " + (confirm ? ChatColor.DARK_GREEN + "correct" : ChatColor.DARK_RED + "false") + ChatColor.GRAY + ".");
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Player '" + player.getName() + "' has not been banned by Reflex.");
                    }
                }
                else {
                    RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND_DATABASE, target);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());

    }

}