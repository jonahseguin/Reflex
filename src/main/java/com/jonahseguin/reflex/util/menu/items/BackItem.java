/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */
package com.jonahseguin.reflex.util.menu.items;

import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link com.jonahseguin.reflex.util.menu.items.StaticMenuItem} that opens the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}'s parent menu if it exists.
 */
public class BackItem extends StaticMenuItem {

    public BackItem() {
        super(ChatColor.RED + "Back", new ItemStack(Material.FENCE_GATE));
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillGoBack(true);
    }
}
