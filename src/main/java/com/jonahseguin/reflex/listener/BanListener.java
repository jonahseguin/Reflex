/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.listener;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.ban.ReflexBan;

import java.util.Calendar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class BanListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        final String uniqueId = e.getUniqueId().toString();

        if (Reflex.getInstance().getBanManager().hasBan(uniqueId)) {
            ReflexBan ban = Reflex.getInstance().getBanManager().getBan(uniqueId);
            if (ban.isActive()) {
                //Either still temp-banned or confirmed to be correctly banned (thus making the ban permanent)

                if (!ban.isConfirmed() && ban.getExpiration() > System.currentTimeMillis()) {
                    //Temporary

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(ban.getExpiration());

                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int month = cal.get(Calendar.MONTH);
                    int hour = cal.get(Calendar.HOUR);
                    int minute = cal.get(Calendar.MINUTE);
                    boolean am = cal.get(Calendar.AM_PM) == Calendar.AM;

                    String expires = "" + month + "/" + day + " @ " + hour + ":" + minute + " " + (am ? "AM" : "PM") + "&7 (" + cal.getTimeZone().getDisplayName() + ")";

                    e.setKickMessage(RLang.format(ReflexLang.BANNED, ban.getViolation().getCheckType().getName(), expires));
                }
                else if (ban.isConfirmed() && ban.isBannedCorrectly()) {
                    //Permanent
                    e.setKickMessage(RLang.format(ReflexLang.BANNED_CONFIRMED, ban.getViolation().getCheckType().getName()));
                }

                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            }
        }

    }

}
