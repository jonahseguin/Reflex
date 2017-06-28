/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */
package com.jonahseguin.reflex.util.menu.items;

import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link com.jonahseguin.reflex.util.menu.items.StaticMenuItem} that closes the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
 */
public class CloseItem extends StaticMenuItem {

    public CloseItem() {
        super(ChatColor.RED + "Close", new ItemStack(Material.RECORD_4));
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
    }
}
