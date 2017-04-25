/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class RDynMenuItem extends MenuItem {

    private RMenuHandler action = null;

    public RDynMenuItem() {
        super("NULL", new ItemStack(Material.DIRT));
    }

    public RDynMenuItem action(RMenuHandler action) {
        this.action = action;
        return this;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        if (action != null) {
            action.onClick(event);
        }
    }

    @Override
    public abstract ItemStack getFinalIcon(Player viewer);
}
