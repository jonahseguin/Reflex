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
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.ban.ReflexBan;
import com.jonahseguin.reflex.menu.PlayerMenu;
import com.jonahseguin.reflex.menu.ViolationMenu;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.TimeUtil;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CmdLookup implements RCommand {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RCmd(name = "reflex lookup player", usage = "/reflex lookup player <player>", minArgs = 1, permission = ReflexPerm.LOOKUP_PLAYER,
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
                    if (sender instanceof Player) {
                        PlayerMenu lookupPlayerMenu = new PlayerMenu(t);
                        lookupPlayerMenu.open(((Player) sender));
                    } else {
                        RLang.send(sender, ReflexLang.PLAYER_ONLY_COMMAND);
                    }
                } else {
                    RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND_DATABASE, sTargetFinal);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }



    @RCmd(name = "reflex lookup ban", usage = "/reflex lookup ban <player>", minArgs = 1, permission = ReflexPerm.LOOKUP_BAN,
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
                        } else {
                            expires = "&e" + TimeUtil.format(ban.getExpiration());
                        }
                    }

                    // Inactive bans
                    Set<ReflexBan> bans = Reflex.getInstance().getBanManager().getBans(reflexPlayer.getUniqueId());

                    Set<ReflexBan> loop = new HashSet<>();
                    loop.addAll(bans);
                    Iterator<ReflexBan> it = loop.iterator();
                    while (it.hasNext()) {
                        ReflexBan next = it.next();
                        if (next.isActive()) {
                            bans.remove(next);
                        }
                    }

                    //TODO: Open Bans GUI (display list)

                } else {
                    RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND_DATABASE, target);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    @RCmd(name = "reflex lookup baninfo", usage = "/reflex lookup baninfo <id>", minArgs = 1, permission = ReflexPerm.LOOKUP_BAN,
            aliases = {"! lookup baninfo", "rx lookup baninfo", "rflex lookup baninfo", "reflex lookup bi", "reflex l bi"}, description = "Lookup details on a reflex ban")
    public void onCmdLookupBanInfo(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        final String id = args.getArg(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                ReflexBan ban = Reflex.getInstance().getBanManager().getBanById(id);
                if (ban != null) {
                    // TODO: Open Ban GUI (individual)
                } else {
                    RLang.send(sender, ReflexLang.BAN_NOT_FOUND, id);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    @RCmd(name = "reflex lookup infraction", usage = "/reflex lookup infraction <id>", minArgs = 1, permission = ReflexPerm.LOOKUP_VIOLATION,
            aliases = {"! lookup infraction", "rx lookup infraction", "rflex lookup infraction", "reflex lookup violationCount", "reflex l violationCount"}, description = "Lookup details on a infraction")
    public void onCmdLookupViolation(RCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();
        final String id = args.getArg(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                AutoMongo mongo = RViolation.selectOne(new Document("_id", id), RViolation.class);

                if (mongo != null) {
                    if (sender instanceof Player) {
                        RViolation vl = (RViolation) mongo;
                        ReflexPlayer reflexPlayer = Reflex.getInstance().getCache().getReflexPlayerByUniqueId(vl.getUniqueId());
                        if (reflexPlayer != null) {
                            ViolationMenu lookupViolationMenu = new ViolationMenu(reflexPlayer, vl);
                            lookupViolationMenu.open(((Player) sender));
                        } else {
                            msg(sender, "&cTarget player in infraction could not be found (" + vl.getUniqueId() + ")");
                        }
                    } else {
                        RLang.send(sender, ReflexLang.PLAYER_ONLY_COMMAND);
                    }
                } else {
                    RLang.send(sender, ReflexLang.VIOLATION_NOT_FOUND, id);
                }
            }
        }.runTaskAsynchronously(Reflex.getInstance());
    }

    private void msg(CommandSender sender, String msg) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } else {
            sender.sendMessage(ChatColor.stripColor(msg));
        }
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
