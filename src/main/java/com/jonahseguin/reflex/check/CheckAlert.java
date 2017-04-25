/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:52.
 * Project: Reflex
 */
@Getter
public class CheckAlert {

    private final String id;
    private final CheckViolation violation;
    private final double tps;
    private final int ping;
    @Setter
    private String detail = "";

    public CheckAlert(String id, CheckViolation violation, double tps, int ping) {
        this.id = id;
        this.violation = violation;
        this.tps = tps;
        this.ping = ping;
    }

    public static void staffMsg(String msg) {
        for (Player pl : Reflex.getOnlinePlayers()) {
            ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
            if (p.isAlertsEnabled() && ReflexPerm.ALERTS.hasPerm(pl)) {
                p.msg(msg);
            }
        }
        Bukkit.getLogger().info(ChatColor.stripColor(msg));
    }

    public static void staffMsg(FancyMessage msg) {
        for (Player pl : Reflex.getOnlinePlayers()) {
            ReflexPlayer p = Reflex.getInstance().getCache().getReflexPlayer(pl);
            if (p.isAlertsEnabled() && ReflexPerm.ALERTS.hasPerm(pl)) {
                msg.send(pl);
            }
        }
        msg.send(Bukkit.getConsoleSender());
    }

    public void sendAlert() {

    }


}
