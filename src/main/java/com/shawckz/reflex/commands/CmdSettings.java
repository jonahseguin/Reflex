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
import com.shawckz.reflex.backend.configuration.ReflexPerm;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.trigger.RTrigger;
import com.shawckz.reflex.util.obj.Alert;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdSettings implements RCommand {

    @RCmd(name = "reflex settings", usage = "/reflex settings <toggle|on|off> <enabled|cancel|freeze|autoban> [check(blank for all)]", permission = ReflexPerm.SETTINGS, description = "Manage settings of checks",
            aliases = {"! settings", "reflex settings", "rx settings", "rflex settings", "reflex setting", "rx setting"}, minArgs = 2)
    public void onCmdSettings(final RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        String mode = args.getArg(0).toLowerCase();
        String setting = args.getArg(1).toLowerCase();

        if (args.getArgs().length == 2) {
            //All checks
            boolean msg = true;
            for (RTrigger trigger : Reflex.getInstance().getTriggerManager().getTriggers().values()) {
                if (!editSetting(sender, trigger.getCheckType(), setting, mode, false)) {
                    msg = false;
                    break;
                }
            }
            if (msg) {
                sender.sendMessage(ChatColor.GRAY + "Updating setting '" + setting + "' for all checks to: " + mode);
                Alert.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.SETTINGS_ALL, setting, mode, sender.getName()));
            }
        }
        else if (args.getArgs().length > 2) {
            //One check
            CheckType checkType = CheckType.fromString(args.getArg(2));
            if (checkType != null) {
                editSetting(sender, checkType, setting, mode, true);
            }
            else {
                sender.sendMessage(ChatColor.RED + "Invalid check '" + args.getArg(2) + "'.");
            }
        }

    }

    private boolean editSetting(CommandSender sender, CheckType check, String setting, String mode, boolean msg) {
        setting = setting.toLowerCase();
        mode = mode.toLowerCase();
        if (setting.equals("enabled") || setting.equals("cancel") || setting.equals("freeze") || setting.equals("autoban")) {
            if (mode.equals("toggle") || mode.equals("on") || mode.equals("off")) {
                if (setting.equals("enabled")) {
                    boolean setValue = (mode.equals("toggle") ? !Reflex.getInstance().getTriggerManager().getTrigger(check).isEnabled() : mode.equals("on"));
                    Reflex.getInstance().getTriggerManager().getTrigger(check).setEnabled(setValue);
                    Reflex.getInstance().getTriggerManager().getTrigger(check).save();
                    if (msg) {
                        sender.sendMessage(ChatColor.GRAY + "Updated setting '" + setting + "' for check '" + check.getName() + "' to: " + mode);
                        Alert.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.SETTINGS, setting, (setValue ? "on" : "off"), check.getName(), sender.getName()));
                    }
                }
                else if (setting.equals("cancel")) {
                    boolean setValue = (mode.equals("toggle") ? !Reflex.getInstance().getTriggerManager().getTrigger(check).isCancel() : mode.equals("on"));
                    Reflex.getInstance().getTriggerManager().getTrigger(check).setCancel(setValue);
                    Reflex.getInstance().getTriggerManager().getTrigger(check).save();
                    if (msg) {
                        sender.sendMessage(ChatColor.GRAY + "Updated setting '" + setting + "' for check '" + check.getName() + "' to: " + mode);
                        Alert.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.SETTINGS, setting, (setValue ? "on" : "off"), check.getName(), sender.getName()));
                    }
                }
                else if (setting.equals("freeze")) {
                    boolean setValue = (mode.equals("toggle") ? !Reflex.getInstance().getTriggerManager().getTrigger(check).isEnabled() : mode.equals("on"));
                    Reflex.getInstance().getTriggerManager().getTrigger(check).setAutobanFreeze(setValue);
                    Reflex.getInstance().getTriggerManager().getTrigger(check).save();
                    if (msg) {
                        sender.sendMessage(ChatColor.GRAY + "Updated setting '" + setting + "' for check '" + check.getName() + "' to: " + mode);
                        Alert.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.SETTINGS, setting, (setValue ? "on" : "off"), check.getName(), sender.getName()));
                    }
                }
                else if (setting.equals("autoban")) {
                    boolean setValue = (mode.equals("toggle") ? !Reflex.getInstance().getTriggerManager().getTrigger(check).isEnabled() : mode.equals("on"));
                    Reflex.getInstance().getTriggerManager().getTrigger(check).setAutoban(setValue);
                    Reflex.getInstance().getTriggerManager().getTrigger(check).save();
                    if (msg) {
                        sender.sendMessage(ChatColor.GRAY + "Updated setting '" + setting + "' for check '" + check.getName() + "' to: " + mode);
                        Alert.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.SETTINGS, setting, (setValue ? "on" : "off"), check.getName(), sender.getName()));
                    }
                }
            }
            else {
                sender.sendMessage(ChatColor.RED + "Mode (argument 1) must be: 'toggle', 'on', or 'off'.");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "Setting (argument 2) must be: 'enabled', 'cancel', 'freeze', or 'autoban'.");
            return false;
        }
        return true;
    }

}
