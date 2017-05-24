/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu.backend;

import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class RMenuHandler {

    public abstract void onClick(ItemClickEvent event);

    public ItemStack getFinalIcon(Player player) {
        return null;
    }

}
