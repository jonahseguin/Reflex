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
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.inspect.RInspectResult;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.Alert;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdInspect implements RCommand {

    @RCmd(name = "reflex inspect", usage = "/reflex inspect <player> <check> <seconds>", minArgs = 3, permission = "reflex.inspect", aliases = {"! inspect", "reflex inspect", "rx inspect", "rflex inspect"},
            description = "Start an inspection on a player")
    public void onCmdInspect(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        String sTarget = args.getArg(0);
        String sCheck = args.getArg(1);
        String sSeconds = args.getArg(2);

        {
            Player t = Bukkit.getPlayer(sTarget);
            if (t != null) {
                sTarget = t.getName();
            }
        }

        final String sTargetFinal = sTarget;

        new BukkitRunnable() {
            public void run() {
                ReflexPlayer reflexPlayer = Reflex.getInstance().getCache().getReflexPlayer(sTargetFinal);
                if (reflexPlayer != null) {
                    CheckType checkType = CheckType.fromString(sCheck);
                    if (checkType != null) {
                        int seconds;
                        try {
                            seconds = Integer.parseInt(sSeconds);
                        }
                        catch (NumberFormatException expected) {
                            sender.sendMessage(ChatColor.RED + "Seconds must be an integer.");
                            return;
                        }

                        if (checkType.isCapture()) {

                            Alert.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) +
                                    RLang.format(ReflexLang.ALERT_INSPECT_MANUAL, sender.getName(), reflexPlayer.getName(), checkType.getName(), seconds + ""));

                            Reflex.getInstance().getDataCaptureManager().startCaptureTask(reflexPlayer, checkType, seconds, checkData -> {
                                RInspectResult result = Reflex.getInstance().getInspectManager().inspect(reflexPlayer, checkType, checkData, seconds);
                                RLang.send(sender, ReflexLang.INSPECT_MANUAL_RESULT, reflexPlayer.getName(), result.getData().getType().toString());
                            });

                        }
                        else {
                            sender.sendMessage(ChatColor.RED + "That check does not support data capture/inspection.");
                        }
                    }
                    else {
                        RLang.send(sender, ReflexLang.CHECK_NOT_FOUND, sCheck);
                    }
                }
                else {
                    RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND, sTargetFinal);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());

    }

}
