/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.util;

import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.bridge.RCheckType;
import com.shawckz.reflex.bridge.RViolation;
import com.shawckz.reflex.core.configuration.RLang;
import com.shawckz.reflex.core.configuration.ReflexLang;
import com.shawckz.reflex.core.configuration.ReflexPerm;
import com.shawckz.reflex.core.player.ReflexCache;
import com.shawckz.reflex.core.player.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
public class Alert {

    private final String id;
    private final ReflexPlayer violator;
    private final RViolation violation;
    private final long time;
    private final RCheckType rCheckType;
    private final CheckType checkType;
    private final double tps;
    private final int vl;
   @Setter private String detail = null;

    public Alert(ReflexPlayer violator, RCheckType rCheckType, CheckType checkType, RViolation violation, int vl) {
        this.violator = violator;
        this.rCheckType = rCheckType;
        this.checkType = checkType;
        this.violation = violation;
        this.id = UUID.randomUUID().toString();
        this.time = System.currentTimeMillis();
        this.tps = Lag.getTPS();
        this.vl = vl;
    }

    public void sendAlert() {
        FancyMessage message = new FancyMessage("");

        if(rCheckType == RCheckType.INSPECT) {
            //TODO
        }
        else if (rCheckType == RCheckType.CHECK) {
            String format;
            if(detail != null) {
                format = RLang.format(ReflexLang.ALERT_PREVENT_DETAIL, violator.getName(), checkType.getName(), detail, vl+"");
            }
            else{
                format = RLang.format(ReflexLang.ALERT_PREVENT, violator.getName(), checkType.getName(), vl+"");
            }
            message.then(format).tooltip(ChatColor.YELLOW + "Click for more information").command("/reflex checkvl " + violation.getId());
        }
        else if (rCheckType == RCheckType.TRIGGER) {
            //TODO
        }
        else{
            throw new ReflexException("Could not send alert: Unknown RCheckType");
        }

        staffMsg(message);
    }

    public static void staffMsg(String msg) {
        for(Player pl : Bukkit.getOnlinePlayers()) {
            ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
            if(p.isAlertsEnabled() && ReflexPerm.ALERTS.hasPerm(pl)) {
                p.msg(msg);
            }
        }
    }

    public static void staffMsg(FancyMessage msg) {
        for(Player pl : Bukkit.getOnlinePlayers()) {
            ReflexPlayer p = ReflexCache.get().getReflexPlayer(pl);
            if(p.isAlertsEnabled() && ReflexPerm.ALERTS.hasPerm(pl)) {
                msg.send(pl);
            }
        }
    }

}
