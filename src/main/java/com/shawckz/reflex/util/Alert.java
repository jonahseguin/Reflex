package com.shawckz.reflex.util;

/*
 * Copyright (c) 2015 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.Violation;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Alert class
 * Used to send alerts and messages to online players(staff..?) with alerts enabled.
 */
public class Alert {

    /**
     * Send an alert to online staff with alerts enabled
     * @param v Violation for alert
     */
    public static void send(Violation v){
        String format = Reflex.getReflexConfig().getAlertFormat();//%player, %totalvl, %check, %vl
        format = format.replaceAll("%player", v.getPlayer().getBukkitPlayer().getDisplayName());
        format = format.replaceAll("%totalvl", ""+v.getTotalVL());
        format = format.replaceAll("%check", v.getCheck().getName());
        format = format.replaceAll("%vl", "" + v.getVl());

        format = ChatColor.translateAlternateColorCodes('&', format);

        FancyMessage msg = new FancyMessage("");
        msg.then(format)
                .tooltip(ChatColor.YELLOW+"Click to inspect violation "+ChatColor.GREEN+v.getId()+
                        ChatColor.YELLOW+" on "+ChatColor.AQUA+v.getPlayer().getBukkitPlayer().getDisplayName())
                .command("/ares violation "+v.getPlayer().getName()+" "+v.getId());
        staffMsg(msg);
    }

    /**
     * Send an alert to online staff with alerts enabled (with extra detail)
     * @param v Violation for alert
     * @param detail Detail for alert
     */
    public static void send(Violation v, String detail){
        String format = Reflex.getReflexConfig().getAlertFormatDetail();//%player, %totalvl, %check, %vl, %detail
        format = format.replaceAll("%player", v.getPlayer().getBukkitPlayer().getDisplayName());
        format = format.replaceAll("%totalvl", ""+v.getTotalVL());
        format = format.replaceAll("%check", v.getCheck().getName());
        format = format.replaceAll("%vl", ""+v.getVl());
        format = format.replaceAll("%detail", detail);

        format = ChatColor.translateAlternateColorCodes('&', format);

        FancyMessage msg = new FancyMessage("");
        msg.then(format)
                .tooltip(ChatColor.YELLOW+"Click to inspect violation "+ChatColor.GREEN+v.getId()+
                        ChatColor.YELLOW+" on "+ChatColor.AQUA+v.getPlayer().getBukkitPlayer().getDisplayName())
                .command("/ares violation "+v.getPlayer().getName()+" "+v.getId());
        staffMsg(msg);
    }

    /**
     * Send a message to online staff / players that have alerts enabled
     * @param msg
     */
    public static void staffMsg(FancyMessage msg){
        for(Player pl : Bukkit.getOnlinePlayers()){
            ReflexPlayer p = ReflexCache.get().getAresPlayer(pl);
            if(p.isAlertsEnabled()){
                msg.send(pl);
            }
        }
    }

    /**
     * Send a message to online staff / players that have alerts enabled
     * @param msg
     */
    public static void staffMsg(String msg){
        for(Player pl : Bukkit.getOnlinePlayers()){
            ReflexPlayer p = ReflexCache.get().getAresPlayer(pl);
            if(p.isAlertsEnabled()){
                pl.sendMessage(msg);
            }
        }
    }


}
