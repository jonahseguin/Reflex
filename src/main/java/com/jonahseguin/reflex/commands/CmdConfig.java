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
import com.jonahseguin.reflex.check.CheckConfig;
import com.jonahseguin.reflex.check.alert.AlertManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdConfig implements RCommand {

    @RCmd(name = "reflex config load", usage = "/reflex config load", minArgs = 0, permission = ReflexPerm.CONFIG_LOAD,
            aliases = {"! config load", "rx config load", "rflex config load",}, description = "Re-load the config from the config file")
    public void onCmdConfigLoad(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        Reflex.getInstance().getReflexConfig().load();

        sender.sendMessage(ChatColor.GRAY + "Loaded the Reflex Configuration from the plugins/Reflex/config.yml file.");

        AlertManager.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.CONFIG_LOAD, sender.getName()));
    }

    @RCmd(name = "reflex config set", usage = "/reflex config set <key> <value>", minArgs = 2, permission = ReflexPerm.CONFIG_SET,
            aliases = {"! config set", "rx config set", "rflex config set",}, description = "Set a value in the configuration")
    public void onCmdConfigSet(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        String key = args.getArg(0).toLowerCase();

        String value = args.getArg(1);

        boolean success = Reflex.getInstance().getReflexConfig().setValue(key, value);

        if (success) {
            sender.sendMessage(ChatColor.GRAY + "Updated config value '" + key + "' to '" + value + "'");
            AlertManager.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.CONFIG_SET, key, value, sender.getName()));
        } else {
            sender.sendMessage(ChatColor.RED + "Failed to set config value '" + key + "' to '" + value + "'.  (type is not string?  path not in config?)");
        }

    }

    @RCmd(name = "reflex config reset", usage = "/reflex config reset [checks?]", permission = ReflexPerm.CONFIG_RESET,
            aliases = {"! config reset", "rx config reset", "rflex config reset",}, description = "Reset configs to default values")
    public void onCmdConfigReset(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        boolean checks = args.getArgs().length > 0;

        Reflex.getInstance().getReflexConfig().resetToDefaults();
        if (checks) {
            Reflex.getInstance().getCheckManager().getChecks().forEach(CheckConfig::resetToDefaults);
            AlertManager.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.CONFIG_RESET_CHECKS));
        } else {
            AlertManager.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.CONFIG_RESET));
        }
        sender.sendMessage(ChatColor.GREEN + "Reset.  " + (checks ? "(including check configs)" : "(only reflex config)"));
    }

}
