/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.menu;

import com.shawckz.reflex.Reflex;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.BackItem;
import ninja.amp.ampmenus.items.CloseItem;
import ninja.amp.ampmenus.menus.ItemMenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LookupPlayerMenu extends ItemMenu {

    private final Player player;

    public LookupPlayerMenu(Player player) {
        super("Reflex - " + player.getName(), Size.THREE_LINE, Reflex.getInstance());
        this.player = player;

        setItem(13, new RMenuItem(ChatColor.GREEN + player.getName(), new ItemStack(Material.SKULL_ITEM), new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {

            }
        }));

        setItem(26, new CloseItem());
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if(parent != null) {
            setItem(25, new BackItem());
        }
    }



}
