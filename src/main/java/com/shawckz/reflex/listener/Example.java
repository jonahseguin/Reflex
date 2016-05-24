/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.listener;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.ban.ReflexBan;
import com.shawckz.reflex.ban.ReflexBanManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Example {

    {

        ReflexBanManager banManager = Reflex.getInstance().getBanManager();

        //Get an *active* (not expired and banned) ban on a specific player's UUID
        ReflexBan reflexBan = banManager.getBan("someUUID");

        if(reflexBan != null) {
            Bukkit.broadcastMessage(
                    ChatColor.GREEN + "Banned for " + reflexBan.getViolation().getCheckType().getName()
            );
        }

    }

}
