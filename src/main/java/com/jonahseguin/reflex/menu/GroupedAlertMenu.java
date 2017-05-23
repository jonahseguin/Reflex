/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.alert.GroupedAlert;
import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;
import com.jonahseguin.reflex.util.menu.items.BackItem;
import com.jonahseguin.reflex.util.menu.items.CloseItem;
import com.jonahseguin.reflex.util.menu.menus.ItemMenu;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jonah Seguin on Tue 2017-05-23 at 14:11.
 * Project: Reflex
 */
public class GroupedAlertMenu extends ItemMenu {

    public GroupedAlertMenu(GroupedAlert alert) {
        super("Reflex - Grouped Alerts", Size.ONE_LINE, Reflex.getInstance());

        // Alert Details, Lookup Player, Player notes, View violations, Cancel autoban

        final GroupedAlertMenu thisMenu = this;

        // Alert details
        setItem(0, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.ANVIL);
                ib.setName(ChatColor.GOLD + "Looking up Grouped Alert");
                ib.addLoreLine(ChatColor.DARK_GRAY + "ID: " + alert.getId());
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.GRAY + "Check: " + ChatColor.AQUA + alert.getCheckType().getName());
                ib.addLoreLine(ChatColor.GRAY + "Detail: " + ChatColor.AQUA + alert.getDetail());
                ib.addLoreLine(ChatColor.GRAY + "VL: " + ChatColor.AQUA + alert.getVl());

                return ib.toItemStack();
            }
        });

        // Violations in this alert
        setItem(2, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.PAPER);
                ib.setName(ChatColor.GOLD + "Violations in this Alert");
                ib.addLoreLine(ChatColor.DARK_GRAY + "Alert Count: " + alert.getAlertSet().count());
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.GREEN + "Click to view all violations/alerts in this alert");

                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setWillClose(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Reflex.getInstance(), () -> {
                    if (event.getPlayer() != null && event.getPlayer().isOnline()) {
                        // TODO: Open violations menu
                        // Make sure to set this menu as the parent

                        event.getPlayer().sendMessage(ChatColor.RED + "Not yet implemented.");
                    }
                }, 3);
            }
        }));

        // Lookup player
        setItem(2, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.COMPASS);
                ib.setName(ChatColor.GOLD + "Lookup Player");
                ib.addLoreLine(ChatColor.DARK_GRAY + "Player: " + alert.getReflexPlayer().getName());
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.GRAY + "Session VL: " + ChatColor.AQUA + alert.getReflexPlayer().getSessionVL());
                ib.addLoreLine(ChatColor.GREEN + "Click for more details on this player");

                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setWillClose(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Reflex.getInstance(), () -> {
                    if (event.getPlayer() != null && event.getPlayer().isOnline()) {
                        PlayerMenu playerMenu = new PlayerMenu(alert.getReflexPlayer());
                        playerMenu.setParent(thisMenu);
                        playerMenu.open(event.getPlayer());
                    }
                }, 3);
            }
        }));


        setItem(8, new CloseItem());
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if (parent != null) {
            setItem(7, new BackItem());
        }
    }

}
