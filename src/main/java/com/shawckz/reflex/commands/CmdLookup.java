/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.commands;

import com.mongodb.BasicDBObject;
import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.command.RCmd;
import com.shawckz.reflex.backend.command.RCmdArgs;
import com.shawckz.reflex.backend.command.RCommand;
import com.shawckz.reflex.backend.configuration.RLang;
import com.shawckz.reflex.backend.configuration.ReflexLang;
import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.ban.ReflexBan;
import com.shawckz.reflex.check.inspect.RInspectResult;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.TimeUtil;
import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdLookup implements RCommand {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RCmd(name = "reflex lookup player", usage = "/reflex lookup player <player>", minArgs = 1, permission = "reflex.lookup.player",
            aliases = {"! lookup player", "rx lookup player", "rflex lookup player", "reflex lookup p", "reflex l p"}, description = "Lookup details on a player")
    public void onCmdLookupPlayer(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        String sTarget = args.getArg(0);
        {
            Player t = Bukkit.getPlayer(sTarget);
            if (t != null) {
                sTarget = t.getName();
            }
        }

        final String sTargetFinal = sTarget;

        new BukkitRunnable() {
            @Override
            public void run() {
                ReflexPlayer t = Reflex.getInstance().getCache().getReflexPlayer(sTargetFinal);
                if (t != null) {
                    RLang.send(sender, ReflexLang.HEADER_FOOTER);
                    msg(sender, "&7Player Lookup - &a" + t.getName());
                    msg(sender, "&eSession VL&7: &9" + t.getSessionVL());
                    msg(sender, "&eBeing auto-banned&7: &9" + Reflex.getInstance().getAutobanManager().hasAutoban(t.getName()));

                    ReflexBan ban = Reflex.getInstance().getBanManager().getBan(t.getUniqueId());

                    msg(sender, "&eBanned by Reflex&7: &9" + (ban != null));

                    if (ban != null) {
                        msg(sender, "&eBanned for&7: &9" + ban.getViolation().getCheckType().getName());
                        msg(sender, "&eBan time&7: &e" + DATE_FORMAT.format(new Date(ban.getTime())));
                        msg(sender, "&eBanned with TPS&7: &9" + ban.getViolation().getData().getTps());
                        msg(sender, "&eBanned with Ping&7: &9" + ban.getViolation().getData().getPing());
                    }

                    RLang.send(sender, ReflexLang.HEADER_FOOTER);
                }
                else {
                    RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND, sTargetFinal);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    @RCmd(name = "reflex lookup inspection", usage = "/reflex lookup inspection <id>", minArgs = 1, permission = "reflex.lookup.inspection",
            aliases = {"! lookup inspection", "rx lookup inspection", "rflex lookup inspection", "reflex lookup i", "reflex l i"}, description = "Lookup details on an inspection")
    public void onCmdLookupInspection(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        final String id = args.getArg(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                AutoMongo mongo = RInspectResult.selectOne(new BasicDBObject("_id", id), RInspectResult.class);
                if (mongo != null && mongo instanceof RInspectResult) {
                    RInspectResult result = (RInspectResult) mongo;
                    ReflexPlayer t = Reflex.getInstance().getCache().getReflexPlayerByUUID(result.getViolation().getUniqueId());
                    RLang.send(sender, ReflexLang.HEADER_FOOTER);
                    msg(sender, "&7Inspection Lookup - &a" + t.getName() + " &7(" + result.getId() + ")");
                    msg(sender, "&eCheck&7: &9" + result.getViolation().getCheckType().getName());
                    msg(sender, "&eResult&7: &9" + result.getData().getType().toString());
                    msg(sender, "&eData Period&7: &9" + result.getInspectionPeriod() + " seconds");
                    msg(sender, "&eDate&7: &9" + DATE_FORMAT.format(new Date(result.getViolation().getTime())));
                   /* if(result.getData() != null) {
                        msg(sender, "&eTPS&7: &9" + result.getViolation().getData().getTps());
                        msg(sender, "&ePing&7: &9" + result.getViolation().getData().getPing());
                    }*/
                    if (result.getData().getDetail() != null) {
                        msg(sender, "&eDetail&7: &9" + result.getData().getDetail());
                    }
                    RLang.send(sender, ReflexLang.HEADER_FOOTER);
                }
                else {
                    msg(sender, "&cNo inspection result found with that id.");
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    @RCmd(name = "reflex lookup ban", usage = "/reflex lookup ban <player>", minArgs = 1, permission = "reflex.lookup.ban",
            aliases = {"! lookup ban", "rx lookup ban", "rflex lookup ban", "reflex lookup b", "reflex l b"}, description = "Show bans on a player")
    public void onCmdLookupBan(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        final String target = args.getArg(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                ReflexPlayer reflexPlayer = Reflex.getInstance().getCache().getReflexPlayer(target);
                if (reflexPlayer != null) {
                    RLang.send(sender, ReflexLang.HEADER_FOOTER);
                    msg(sender, "&7Reflex Bans on &e" + reflexPlayer.getName() + " &9(click for details)");
                    msg(sender, " ");
                    boolean empty = true;
                    if (Reflex.getInstance().getBanManager().hasBan(reflexPlayer.getUniqueId())) {
                        empty = false;
                        msg(sender, "&6Found &aactive &6Reflex Ban:");

                        ReflexBan ban = Reflex.getInstance().getBanManager().getBan(reflexPlayer.getUniqueId());

                        String expires;
                        if (ban.isConfirmed()) {
                            expires = "&cNever (confirmed)";
                        }
                        else {
                            expires = "&e" + TimeUtil.format(ban.getExpiration());
                        }

                        new FancyMessage(color("&7- &e" + ban.getViolation().getCheckType().getName() + " &9#" + ban.getId().substring(0, 6) + " &7[" + (ban.isActive() ? "&aActive" : "&cInactive") + "&7] &7(Expires " + expires + "&7)"))
                                .tooltip(color("&eClick for ban information"))
                                .command("/reflex lookup baninfo " + ban.getId())
                                .send(sender);

                    }

                    Set<ReflexBan> bans = Reflex.getInstance().getBanManager().getBans(reflexPlayer.getUniqueId());
                    {
                        Set<ReflexBan> loop = new HashSet<>();
                        loop.addAll(bans);
                        Iterator<ReflexBan> it = loop.iterator();
                        while (it.hasNext()) {
                            ReflexBan next = it.next();
                            if (next.isActive()) {
                                bans.remove(next);
                            }
                        }
                    }

                    if (!bans.isEmpty()) {
                        msg(sender, " ");
                        empty = false;
                        msg(sender, "&6Found &e" + bans.size() + "&6 inactive Reflex Bans:");

                        bans.stream().forEach(ban -> {

                            new FancyMessage(color("&7- &e" + ban.getViolation().getCheckType().getName() + " &9#" + ban.getId().substring(0, 6) + " &7[" + (ban.isActive() ? "&aActive" : "&cInactive") + "&7]"))
                                    .tooltip(color("&eClick for ban information"))
                                    .command("/reflex lookup baninfo " + ban.getId())
                                    .send(sender);
                        });

                    }

                    if (empty) {
                        msg(sender, "&cNo Reflex Bans on record for player '" + reflexPlayer.getName() + "'.");
                    }
                    RLang.send(sender, ReflexLang.HEADER_FOOTER);
                }
                else {
                    RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND_DATABASE, target);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    @RCmd(name = "reflex lookup baninfo", usage = "/reflex lookup baninfo <id>", minArgs = 1, permission = "reflex.lookup.ban",
            aliases = {"! lookup baninfo", "rx lookup baninfo", "rflex lookup baninfo", "reflex lookup bi", "reflex l bi"}, description = "Lookup details on a reflex ban")
    public void onCmdLookupBanInfo(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        final String id = args.getArg(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                ReflexBan ban = Reflex.getInstance().getBanManager().getBanById(id);
                if (ban != null) {
                    RLang.send(sender, ReflexLang.HEADER_FOOTER);
                    msgBanInfo(sender, ban);

                    RLang.send(sender, ReflexLang.HEADER_FOOTER);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    private void msgBanInfo(CommandSender sender, ReflexBan ban) {
        ReflexPlayer player = Reflex.getInstance().getCache().getReflexPlayerByUUID(ban.getUniqueId());
        msg(sender, (ban.isActive() ? "&2[ACTIVE] " : "") + "&7Ban Lookup - &a" + player.getName());
        msg(sender, "&eBanned&7: &9" + ban.isBanned());
        msg(sender, "&eCheck&7: &9" + ban.getViolation().getCheckType().getName());
        msg(sender, "&eBanned on&7: &9" + TimeUtil.format(ban.getTime()));
        if (ban.isConfirmed() && ban.isBannedCorrectly()) {
            msg(sender, "&eExpires&7: &9Never &7(Confirmed)");
        }
        else {
            msg(sender, "&eExpires&7: &9" + TimeUtil.format(ban.getExpiration()));
        }
        msg(sender, "&eConfirmed&7: &9" + ban.isConfirmed());
        if (ban.isConfirmed()) {
            msg(sender, "&eWas banned falsely&7: &9" + !ban.isBannedCorrectly());
        }
    }

    private void msg(CommandSender sender, String msg) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
        else {
            sender.sendMessage(ChatColor.stripColor(msg));
        }
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}