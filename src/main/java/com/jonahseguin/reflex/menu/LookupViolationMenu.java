/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.oldchecks.base.RViolation;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.TimeUtil;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.BackItem;
import ninja.amp.ampmenus.items.CloseItem;
import ninja.amp.ampmenus.menus.ItemMenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LookupViolationMenu extends ItemMenu {

    public LookupViolationMenu(ReflexPlayer player, RViolation vl) {
        super("Reflex - " + player.getName(), Size.ONE_LINE, Reflex.getInstance());

        final LookupViolationMenu thisMenu = this;

        setItem(0, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(player.getName());

                ib.setName(ChatColor.translateAlternateColorCodes('&', "&9Violation &7#" + vl.getId()));

                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "Unique ID: " + vl.getUniqueId());
                ib.addLoreLine(ChatColor.DARK_GRAY + "Player: " + player.getName());
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.YELLOW + "Click to lookup this player");
                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                Player p = event.getPlayer();
                LookupPlayerMenu lookupPlayerMenu = new LookupPlayerMenu(player);
                lookupPlayerMenu.setParent(thisMenu);
                lookupPlayerMenu.open(p);
            }
        }));


        setItem(1, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.PAPER);

                ib.setName(ChatColor.BLUE + "Check");
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "" + vl.getCheckType().getName() + " [" + vl.getSource().toString() + "]");

                return ib.toItemStack();
            }
        });

        setItem(2, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.PORTAL);

                ib.setName(ChatColor.BLUE + "Violation Level");
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "VL: " + vl.getVl());

                return ib.toItemStack();
            }
        });

        setItem(3, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.EXP_BOTTLE);

                ib.setName(ChatColor.BLUE + "Time");
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "" + TimeUtil.format(vl.getTime()));

                return ib.toItemStack();
            }
        });

        setItem(8, new CloseItem());
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if (parent != null) {
            setItem(8, new BackItem());
        }
    }


}
