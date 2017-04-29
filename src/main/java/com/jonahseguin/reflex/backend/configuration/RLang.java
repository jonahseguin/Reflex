/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.backend.configuration;

import com.jonahseguin.reflex.Reflex;
import org.bukkit.command.CommandSender;

public class RLang {

    public static String format(ReflexLang factionLang, String... args) {
        return Reflex.getInstance().getLang().getFormattedLang(factionLang, args);
    }

    public static void send(CommandSender player, ReflexLang lang, String... args) {
        String s = format(lang, args);
        if (!s.equals("")) {
            player.sendMessage(s);
        }
    }

}
