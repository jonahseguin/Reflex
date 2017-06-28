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
import com.jonahseguin.reflex.check.alert.Alert;
import com.jonahseguin.reflex.check.alert.AlertType;
import com.jonahseguin.reflex.check.alert.CheckAlert;
import com.jonahseguin.reflex.check.alert.GroupedAlert;
import com.jonahseguin.reflex.menu.alert.AlertMenu;
import com.jonahseguin.reflex.menu.alert.GroupedAlertMenu;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on Tue 2017-05-23 at 14:07.
 * Project: Reflex
 */
public class CmdAlert implements RCommand {

    @RCmd(name = "reflex alert", usage = "/reflex alert <id>", description = "Get insight on an alert", permission = ReflexPerm.LOOKUP_ALERT,
            playerOnly = true, minArgs = 1, aliases = {"! alert", "rx alert", "rflex alert"})
    public void onCmdCheck(final RCmdArgs args) {
        final Player player = args.getSender().getPlayer();
        String id = args.getArg(0);
        Alert alert = Reflex.getInstance().getAlertManager().getAlert(id);
        if (alert != null) {
            if (alert.getAlertType().equals(AlertType.GROUPED)) {
                if (alert instanceof GroupedAlert) {
                    GroupedAlert groupedAlert = (GroupedAlert) alert;
                    GroupedAlertMenu groupedAlertMenu = new GroupedAlertMenu(groupedAlert);
                    groupedAlertMenu.open(player);
                } else {
                    player.sendMessage(ChatColor.RED + "(Developer error) GROUPED alert is not instance of GroupedAlert?");
                }
            } else if (alert.getAlertType().equals(AlertType.SINGLE)) {
                if (alert instanceof CheckAlert) {
                    CheckAlert checkAlert = (CheckAlert) alert;
                    AlertMenu alertMenu = new AlertMenu(checkAlert);
                    alertMenu.open(player);
                } else {
                    player.sendMessage(ChatColor.RED + "(Developer error) SINGLE alert is not instance of CheckAlert?");
                }
            }
        } else {
            RLang.send(player, ReflexLang.ALERT_NOT_FOUND, id);
        }
    }

}
