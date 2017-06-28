/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.obj;

import org.bukkit.ChatColor;

public class MsgUtil {

    public static String colorBoolean(boolean a) {
        return a ? ChatColor.GREEN + "" + a : ChatColor.RED + "" + a;
    }

}
