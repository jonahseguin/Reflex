/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */
package com.jonahseguin.reflex.util.menu.menus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Allows you to set the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} that created the Inventory as the Inventory's holder.
 */
public class MenuHolder implements InventoryHolder {
    private com.jonahseguin.reflex.util.menu.menus.ItemMenu menu;
    private Inventory inventory;

    public MenuHolder(com.jonahseguin.reflex.util.menu.menus.ItemMenu menu, Inventory inventory) {
        this.menu = menu;
        this.inventory = inventory;
    }

    /**
     * Gets the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} holding the Inventory.
     *
     * @return The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} holding the Inventory.
     */
    public com.jonahseguin.reflex.util.menu.menus.ItemMenu getMenu() {
        return menu;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
