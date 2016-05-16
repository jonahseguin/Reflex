/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.commands;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.command.RCmd;
import com.shawckz.reflex.backend.command.RCmdArgs;
import com.shawckz.reflex.backend.command.RCommand;
import com.shawckz.reflex.backend.configuration.ReflexPerm;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.util.obj.Lag;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;

public class CmdReflex implements RCommand {

    @RCmd(name = "reflex", aliases = {"!", "reflex", "rx", "rflex"}, usage = "/reflex", description = "Reflex general command")
    public void onCmd(RCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();
        sendHeader(sender);
        if (ReflexPerm.USE.hasPerm(sender)) {
            if(args.getArgs().length > 0) {
                msg(sender, " ");
                msg(sender, "&cWhoops, looks like the sub-command you tried doesn't exist.  Type /reflex for commands.");
            }
            else {
                msg(sender, " ");
                msg(sender, "&7- /reflex &9status");
                msg(sender, "&7- /reflex &9inspect &7<player> <check> <seconds>");
                msg(sender, "&7- /reflex &9cancel &7<player>");
                msg(sender, "&7- /reflex &9lookup &eplayer &7<player>");
                msg(sender, "&7- /reflex &9lookup &einspection &7<id>");
                msg(sender, "&7- /reflex &9lookup &eviolation&7 <id>");
                msg(sender, "&7- /reflex &9unban &7<player>");
                msg(sender, "&7- /reflex &9settings &e<toggle|on|off> &7<enabled|cancel|freeze|autoban> <check>");
                msg(sender, "&7- /reflex &9config &eload");
                msg(sender, "&7- /reflex &9config set &7<key> <value>");
            }
        }
    }

    @RCmd(name = "reflex status", aliases = {"! status", "reflex status", "rx status", "rflex status"}, permission = "reflex.use")
    public void onCmdStatus(RCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();
        sendHeader(sender);

        msg(sender, " ");
        msg(sender, "&7TPS: &9" + Lag.getTPS() + " &8(" + Math.round(Lag.getLagPerecentage()) + "% lag)");
        sendChecksEnabled(sender);
        sendChecksCancel(sender);
        sendChecksAutoban(sender);
        sendChecksFreeze(sender);

    }

    public static void sendHeader(CommandSender sender) {
        msg(sender, "&7*** &cReflex &7v" + Reflex.getInstance().getDescription().getVersion() + " &7***");
        msg(sender, "&8Developed by Shawckz - http://shawckz.com/project/reflex");
    }

    private static void msg(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    private void sendChecksEnabled(CommandSender sender) {
        String s = "&7Checks: ";
        for(RTrigger check : Reflex.getInstance().getTriggerManager().getTriggers().values()) {
            if (check.isEnabled()) {
                s += "&a" + check.getCheckType().getName() + "&7, ";
            }
            else{
                s += "&c" + check.getCheckType().getName() + "&7, ";
            }
        }
        if(s.length() >= 4) {
            s = s.substring(0, s.length() - 4);
        }
        msg(sender, s);
    }

    private void sendChecksCancel(CommandSender sender) {
        String s = "&7Cancel: ";
        for(RTrigger check : Reflex.getInstance().getTriggerManager().getTriggers().values()) {
            if (check.isCancel()) {
                s += "&a" + check.getCheckType().getName() + "&7, ";
            }
            else{
                s += "&c" + check.getCheckType().getName() + "&7, ";
            }
        }
        if(s.length() >= 4) {
            s = s.substring(0, s.length() - 4);
        }
        msg(sender, s);
    }

    private void sendChecksAutoban(CommandSender sender) {
        String s = "&7Autoban: ";
        for(RTrigger check : Reflex.getInstance().getTriggerManager().getTriggers().values()) {
            if (check.isAutoban()) {
                s += "&a" + check.getCheckType().getName() + "&7, ";
            }
            else{
                s += "&c" + check.getCheckType().getName() + "&7, ";
            }
        }
        if(s.length() >= 4) {
            s = s.substring(0, s.length() - 4);
        }
        msg(sender, s);
    }

    private void sendChecksFreeze(CommandSender sender) {
        String s = "&7Autoban Freeze: ";
        for(RTrigger check : Reflex.getInstance().getTriggerManager().getTriggers().values()) {
            if (check.isAutobanFreeze()) {
                s += "&a" + check.getCheckType().getName() + "&7, ";
            }
            else{
                s += "&c" + check.getCheckType().getName() + "&7, ";
            }
        }
        if(s.length() >= 4) {
            s = s.substring(0, s.length() - 4);
        }
        msg(sender, s);
    }

}
