/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu;

import ninja.amp.ampmenus.items.BackItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jonah Seguin on Sat 2017-05-13 at 11:18.
 * Project: Reflex
 */
public class SelectViolationsMenu extends ItemMenu {

    public SelectViolationsMenu(String name, Size size, JavaPlugin plugin, ItemMenu parent) {
        super(name, size, plugin, parent);
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if (parent != null) {
            setItem(53, new BackItem());
        }
    }

}
