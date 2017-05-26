/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu.alert;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.ban.Autoban;
import com.jonahseguin.reflex.check.alert.AlertManager;
import com.jonahseguin.reflex.check.alert.CheckAlert;
import com.jonahseguin.reflex.menu.backend.RDynMenuItem;
import com.jonahseguin.reflex.menu.backend.RMenuHandler;
import com.jonahseguin.reflex.menu.other.PlayerMenu;
import com.jonahseguin.reflex.menu.violation.ViolationsMenu;
import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;
import com.jonahseguin.reflex.util.menu.items.BackItem;
import com.jonahseguin.reflex.util.menu.items.CloseItem;
import com.jonahseguin.reflex.util.menu.menus.ItemMenu;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jonah Seguin on Tue 2017-05-23 at 14:11.
 * Project: Reflex
 */
public class AlertMenu extends ItemMenu {

    public AlertMenu(CheckAlert alert) {
        super("Reflex - Alert", Size.ONE_LINE, Reflex.getInstance());

        // Alert Details, Lookup Player, Player notes, View violations, Cancel autoban

        final AlertMenu thisMenu = this;

        // Alert details
        setItem(0, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.ANVIL);
                ib.setName(ChatColor.GOLD + "Looking up Alert");
                ib.addLoreLine(ChatColor.DARK_GRAY + "ID: " + alert.getId());
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.GRAY + "Check: " + ChatColor.AQUA + alert.getCheckType().getName());
                ib.addLoreLine(ChatColor.GRAY + "Detail: " + ChatColor.AQUA + alert.getDetail());
                ib.addLoreLine(ChatColor.GRAY + "VL: " + ChatColor.AQUA + alert.getVl());
                ib.addLoreLine(ChatColor.GRAY + "TPS: " + ChatColor.AQUA + alert.getTps());
                ib.addLoreLine(ChatColor.GRAY + "Ping: " + ChatColor.AQUA + alert.getPing());

                return ib.toItemStack();
            }
        });

        // Lookup player
        setItem(1, new RDynMenuItem() {
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

        // Player Notes
        setItem(2, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.PAPER);
                ib.setName(ChatColor.GOLD + "Player Notes");
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.RED + "Coming soon"); // TODO: implement notes

                return ib.toItemStack();
            }
        });

        // Violations for this check
        setItem(3, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.ENCHANTED_BOOK);
                ib.setName(ChatColor.GOLD + "View Violations for this Check");
                ib.addLoreLine(ChatColor.GRAY + "Check: " + ChatColor.AQUA + alert.getCheckType().getName());
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.GRAY + "Violation Count for this Check: " + ChatColor.AQUA +
                        alert.getReflexPlayer().getRecord().getViolationCount(alert.getCheckType()));
                ib.addLoreLine(ChatColor.GREEN + "Click to view all violations for this player");

                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setWillClose(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Reflex.getInstance(), () -> {
                    if (event.getPlayer() != null && event.getPlayer().isOnline()) {
                        ViolationsMenu violationsMenu = new ViolationsMenu(alert.getReflexPlayer(), alert.getCheckType());
                        violationsMenu.setParent(AlertMenu.this);
                        violationsMenu.open(event.getPlayer());
                    }
                }, 3);
            }
        }));

        // Auto-ban
        setItem(4, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.GOLDEN_APPLE);
                ib.setName(ChatColor.GOLD + "Auto-Ban Status");
                ib.addLoreLine(ChatColor.GRAY + "Is banned: " + (alert.getReflexPlayer().isBanned() ? ChatColor.GREEN + "YES" : ChatColor.RED + " NO"));
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.GRAY + "Is being auto-banned: " + ChatColor.AQUA + alert.getReflexPlayer().isBeingAutobanned());
                if (alert.getReflexPlayer().isBeingAutobanned()) {
                    ib.addLoreLine(ChatColor.GREEN + "LEFT Click to cancel auto-ban");
                    ib.addLoreLine(ChatColor.RED + "RIGHT click to ban now");
                }

                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                if (alert.getReflexPlayer().isBeingAutobanned()) {
                    event.setAcceptedClickTypes(ClickType.LEFT, ClickType.RIGHT);
                    if (event.getClickType().equals(ClickType.LEFT)) {
                        Reflex.getInstance().getAutobanManager().removeAutoban(alert.getReflexPlayer().getName());
                        AlertManager.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.CANCEL, alert.getReflexPlayer().getName()));
                    } else if (event.getClickType().equals(ClickType.RIGHT)) {
                        Autoban autoban = Reflex.getInstance().getAutobanManager().getAutoban(alert.getReflexPlayer().getName());
                        autoban.ban();
                    }
                }
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
