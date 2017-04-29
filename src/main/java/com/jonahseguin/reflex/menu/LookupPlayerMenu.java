/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.BackItem;
import ninja.amp.ampmenus.items.CloseItem;
import ninja.amp.ampmenus.items.StaticMenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LookupPlayerMenu extends ItemMenu {

    private final ReflexPlayer player;

    public LookupPlayerMenu(ReflexPlayer player) {
        super("Reflex - " + player.getName(), Size.ONE_LINE, Reflex.getInstance());
        this.player = player;

        final LookupPlayerMenu thisMenu = this;

        setItem(0, new StaticMenuItem(ChatColor.BLUE + player.getName(), new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal())).setSkullOwner(player.getName()).toItemStack(),
                " ", ChatColor.DARK_GRAY + (Bukkit.getPlayer(player.getName()) != null ? ChatColor.GREEN + "Online" : ChatColor.RED + "Offline")));

        setItem(1, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                        .setName(ChatColor.BLUE + "Session VL")
                        .addLoreLine(" ")
                        .addLoreLine("" + ChatColor.DARK_GRAY + player.getSessionVL() + " VL (since last server startup)")
                        .toItemStack();
            }
        });

        setItem(2, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.PAPER);
                ib.setName(ChatColor.BLUE + "Violation Levels");
                int i = 0;
                ib.addLoreLine(" ");
                for (CheckType checkType : CheckType.values()) {
                    if (player.hasVL(checkType)) {
                        int vl = player.getVL(checkType);
                        ib.addLoreLine(ChatColor.DARK_GRAY + checkType.getName() + ": " + ChatColor.GRAY + vl);
                        i++;
                    }
                }
                if (i == 0) {
                    ib.addLoreLine(ChatColor.DARK_GRAY + "No violation levels.");
                }
                return ib.toItemStack();
            }
        });

        setItem(3, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                return new ItemBuilder(Material.FIREBALL)
                        .setName(ChatColor.BLUE + "Is Being Auto-Banned")
                        .addLoreLine(" ")
                        .addLoreLine(ChatColor.DARK_GRAY + (Reflex.getInstance().getAutobanManager().hasAutoban(player.getName()) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"))
                        .toItemStack();
            }
        });

        setItem(4, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.PAPER);
                ib.setName(ChatColor.BLUE + "Is Banned");
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "Not yet implemented...");

                return ib.toItemStack();
            }
        });

        setItem(5, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.PORTAL);
                ib.setName(ChatColor.BLUE + "All Violations");
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "Click to view all violations");
                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                Player p = event.getPlayer();
                ShowViolationsMenu showViolationsMenu = new ShowViolationsMenu(player);
                showViolationsMenu.setParent(thisMenu);
                showViolationsMenu.open(p);
            }
        }));

        setItem(6, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                return new ItemBuilder(Material.BOOK_AND_QUILL)
                        .setName(ChatColor.BLUE + "Issue Inspection")
                        .addLoreLine(" ")
                        .addLoreLine(ChatColor.DARK_GRAY + "Issue a manual inspection on a given oldchecks")
                        .addLoreLine(ChatColor.DARK_GRAY + "for a given amount of time")
                        .toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.getPlayer().sendMessage(ChatColor.RED + "This feature is not yet implemented.  Yell at Shawckz.");
            }
        }));

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
