/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.commands;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.command.RCmd;
import com.jonahseguin.reflex.backend.command.RCmdArgs;
import com.jonahseguin.reflex.backend.command.RCmdWrapper;
import com.jonahseguin.reflex.backend.command.RCommand;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Lag;
import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdReflex implements RCommand {

    public static void sendHeader(CommandSender sender) {
        msg(sender, "&7*** &cReflex &7v" + Reflex.getInstance().getDescription().getVersion() + " &7***");
        msg(sender, "&8Developed by Jonah Seguin (Shawckz) - https://shawckz.com & https://cheats.rip");
    }

    private static void msg(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    private static void msg(CommandSender sender, FancyMessage msg) {
        msg.send(sender);
    }

    @RCmd(name = "alerts", usage = "/alerts [on|off]", description = "Toggle alerts on or off", permission = ReflexPerm.ALERTS, playerOnly = true)
    public void onCmdAlerts(RCmdArgs args) {
        Player p = args.getSender().getPlayer();
        ReflexPlayer rp = Reflex.getInstance().getCache().getReflexPlayer(p);

        boolean toSet = !rp.isAlertsEnabled();

        if (args.getArgs().length > 0) {
            String s = args.getArg(0);
            if (s.equalsIgnoreCase("on")) {
                toSet = true;
            } else if (s.equalsIgnoreCase("off")) {
                toSet = false;
            } else {
                p.sendMessage(ChatColor.RED + "Incorrect usage: Argument 1 must be 'on' or 'off'.");
                return;
            }
        }

        rp.setAlertsEnabled(toSet);
        if (toSet) {
            RLang.send(p, ReflexLang.ALERTS_ENABLED);
        } else {
            RLang.send(p, ReflexLang.ALERTS_DISABLED);
        }

    }

    @RCmd(name = "reflex", aliases = {"!", "reflex", "rx", "rflex"}, usage = "/reflex", description = "Reflex commands and info")
    public void onCmd(RCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();
        RLang.send(sender, ReflexLang.HEADER_FOOTER);
        sendHeader(sender);
        if (ReflexPerm.USE.hasPerm(sender)) {
            if (args.getArgs().length > 0) {
                msg(sender, " ");
                msg(sender, "&cWhoops, looks like the sub-command you tried doesn't exist.  Type /reflex for commands.");
            } else {
                msg(sender, "&9Commands &7(click for details)");
                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9status"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp status"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9cancel &7<player>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp cancel"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9lookup &eplayer &7<player>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp lookup player"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9lookup &eban&7 <player>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp lookup ban"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9lookup &ebaninfo &7<id>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp lookup baninfo"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9checks"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp checks"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9check &7<check>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp check"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9unban &7<player>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp unban"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9confirmban &7<player> <true|false>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp confirmban"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9note &7<player> <note>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp note"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9notes &7<player>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp notes"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9settings &7<toggle|on|off> <enabled|cancel|freeze|autoban> [checks(blank for all)]"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp settings"));//Done

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9config &eload"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp config load"));

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9config set &7<key> <value>"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp config set"));

                msg(sender, new FancyMessage(ChatColor.translateAlternateColorCodes('&', "&7- /reflex &9config reset &7[checks?]"))
                        .tooltip(ChatColor.GRAY + "Click for command details")
                        .command("/reflex cmdhelp config reset"));
            }
        }
        RLang.send(sender, ReflexLang.HEADER_FOOTER);
    }

    @RCmd(name = "reflex status", aliases = {"! status", "reflex status", "rx status", "rflex status"}, permission = ReflexPerm.USE, description = "Plugin/server information and oldchecks statuses", usage = "/reflex status")
    public void onCmdStatus(RCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();
        RLang.send(sender, ReflexLang.HEADER_FOOTER);
        sendHeader(sender);

        msg(sender, " ");
        msg(sender, "&7TPS: &9" + Lag.getTPS() + " &8(" + Math.round(Lag.getLagPerecentage()) + "% lag)");

        // TODO: Status Menu

        RLang.send(sender, ReflexLang.HEADER_FOOTER);
    }

    @RCmd(name = "reflex cmdhelp", minArgs = 1, aliases = {"! cmdhelp", "rx cmdhelp"}, permission = ReflexPerm.USE, description = "Command information and help", usage = "/reflex cmdhelp <command>")
    public void onCmdHelp(RCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();

        String cmd = "reflex " + args.getBuiltArgs(0).toLowerCase();
        cmd = cmd.replaceAll(" ", ".");

        if (Reflex.getInstance().getCommandHandler().hasCommandWrapper(cmd)) {
            RCmdWrapper wrapper = Reflex.getInstance().getCommandHandler().getCommand(cmd);
            RLang.send(sender, ReflexLang.HEADER_FOOTER);

            msg(sender, "&9Command Help&7: &e" + wrapper.getName());
            if (wrapper.getAliases().length > 0) {
                msg(sender, "&7" + Arrays.toString(wrapper.getAliases()) + "");
            }
            if (!wrapper.getDescription().equals("")) {
                msg(sender, "&e\"" + wrapper.getDescription() + "\"");
            }
            msg(sender, " ");
            if (!wrapper.getUsage().equals("")) {
                msg(sender, "&7Usage: &e" + wrapper.getUsage());
            }
            if (wrapper.getMinArgs() > 0) {
                msg(sender, "&7Minimum arguments: &e" + wrapper.getMinArgs());
            }
            msg(sender, "&7Permission: &e" + wrapper.getPermission());
            msg(sender, "&7Player only: &e" + wrapper.isPlayerOnly());
            RLang.send(sender, ReflexLang.HEADER_FOOTER);
        } else {
            msg(sender, ChatColor.RED + "No reflex-command registered with key '" + cmd + "'.");
        }

    }

    private void sendChecksEnabled(CommandSender sender) {
        FancyMessage fm = new FancyMessage(color("&7Checks: "));
        for (Check check : Reflex.getInstance().getCheckManager().getChecks()) {
            if (check.isEnabled()) {
                fm.then(color("&a" + check.getCheckType().getName() + "&7, "))
                        .tooltip(color("&eClick to toggle &9enabled &7[" + check.getCheckType().getName() + "]"))
                        .command("/reflex settings toggle enabled " + check.getCheckType().getName());
            } else {
                fm.then(color("&c" + check.getCheckType().getName() + "&7, "))
                        .tooltip(color("&eClick to toggle &9enabled &7[" + check.getCheckType().getName() + "]"))
                        .command("/reflex settings toggle enabled " + check.getCheckType().getName());
            }
        }
        fm.send(sender);
    }

    private void sendChecksCancel(CommandSender sender) {
        String s = "&7Cancel: ";
        for (Check check : Reflex.getInstance().getCheckManager().getChecks()) {
            if (check.isCancel()) {
                s += "&a" + check.getCheckType().getName() + "&7, ";
            } else {
                s += "&c" + check.getCheckType().getName() + "&7, ";
            }
        }
        if (s.length() >= 4) {
            s = s.substring(0, s.length() - 4);
        }
        msg(sender, s);
    }

    private void sendChecksAutoban(CommandSender sender) {
        String s = "&7Autoban: ";
        for (Check check : Reflex.getInstance().getCheckManager().getChecks()) {
            if (check.isAutoban()) {
                s += "&a" + check.getCheckType().getName() + "&7, ";
            } else {
                s += "&c" + check.getCheckType().getName() + "&7, ";
            }
        }
        if (s.length() >= 4) {
            s = s.substring(0, s.length() - 4);
        }
        msg(sender, s);
    }

    private void sendChecksFreeze(CommandSender sender) {
        String s = "&7Autoban Freeze: ";
        for (Check check : Reflex.getInstance().getCheckManager().getChecks()) {
            if (check.isAutobanFreeze()) {
                s += "&a" + check.getCheckType().getName() + "&7, ";
            } else {
                s += "&c" + check.getCheckType().getName() + "&7, ";
            }
        }
        if (s.length() >= 4) {
            s = s.substring(0, s.length() - 4);
        }
        msg(sender, s);
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
