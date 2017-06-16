/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu.backend;

import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;
import com.jonahseguin.reflex.util.menu.items.MenuItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RMenuItem extends MenuItem {

    private final RMenuHandler action;

    public RMenuItem(String displayName, ItemStack icon, RMenuHandler action, String... lore) {
        super(displayName, icon, lore);
        this.action = action;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        action.onClick(event);
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        ItemStack i = action.getFinalIcon(player);
        if (i == null) {
            return super.getFinalIcon(player);
        }
        return i;
    }
}
