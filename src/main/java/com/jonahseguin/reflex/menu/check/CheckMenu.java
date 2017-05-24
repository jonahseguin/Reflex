/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.commands.CmdSettings;
import com.jonahseguin.reflex.menu.backend.RDynMenuItem;
import com.jonahseguin.reflex.menu.backend.RMenuHandler;
import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;
import com.jonahseguin.reflex.util.menu.items.BackItem;
import com.jonahseguin.reflex.util.menu.items.CloseItem;
import com.jonahseguin.reflex.util.menu.menus.ItemMenu;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jonah Seguin on Mon 2017-05-22 at 20:27.
 * Project: Reflex
 */
public class CheckMenu extends ItemMenu {

    /*
    [1]  0  1  2  3  4  5  6  7  8
    [2]  9  10 11 12 13 14 15 16 17
    [3]  18 19 20 21 22 23 24 25 26
    [4]  27 28 29 30 31 32 33 34 35
    [5]  36 37 38 39 40 41 42 43 44
    [6]  45 46 47 48 49 50 51 52 53
     */

    public CheckMenu(CheckType checkType) {
        super("Reflex - " + checkType.getName(), Size.SIX_LINE, Reflex.getInstance());

        final Check check = Reflex.getInstance().getCheckManager().getCheck(checkType);

        setItem(4, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.ANVIL);
                final String nameEnabled = (check.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + checkType.getName();
                ib.setName(ChatColor.translateAlternateColorCodes('&', "&9Check " + nameEnabled));
                ib.addLoreLine(ChatColor.GRAY + check.description());

                return ib.toItemStack();
            }
        });

        //20, 22, 24 --> enabled, cancel, auto-ban

        //Enabled
        setItem(20, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                final Material material = (check.isEnabled() ? Material.REDSTONE_TORCH_ON : Material.REDSTONE_TORCH_OFF);
                ItemBuilder ib = new ItemBuilder(material);
                ib.setName(ChatColor.GRAY + "Enabled: " + enabled(check.isEnabled()));
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "This setting controls whether the check is active.");
                ib.addLoreLine(ChatColor.GOLD + "Click to " + (check.isEnabled() ? "disable" : "enable") + " this check");
                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setWillUpdate(true);
                CmdSettings.editSetting(event.getPlayer(), checkType, "enabled", "toggle", true);
            }
        }));

        //Cancel
        setItem(22, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                final Material material = (check.isCancel() ? Material.GOLD_INGOT : Material.IRON_INGOT);
                ItemBuilder ib = new ItemBuilder(material);
                ib.setName(ChatColor.GRAY + "Cancel: " + enabled(check.isCancel()));
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "When cancelling is enabled, (if supported) actions will be set-back on failure.");
                ib.addLoreLine(ChatColor.GOLD + "Click to " + (check.isCancel() ? "disable" : "enable") + " cancelling");
                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setWillUpdate(true);
                CmdSettings.editSetting(event.getPlayer(), checkType, "cancel", "toggle", true);
            }
        }));

        //Auto-ban
        setItem(24, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                final Material material = (check.isAutoban() ? Material.EMERALD : Material.REDSTONE);
                ItemBuilder ib = new ItemBuilder(material);
                ib.setName(ChatColor.GRAY + "Enabled: " + enabled(check.isAutoban()));
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "When auto-ban is enabled, a player will be banned upon receiving an infraction.");
                ib.addLoreLine(ChatColor.GOLD + "Click to " + (check.isAutoban() ? "disable" : "enable") + " auto-ban");
                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setWillUpdate(true);
                CmdSettings.editSetting(event.getPlayer(), checkType, "autoban", "toggle", true);
            }
        }));

        //38, 40, 42 --> fails, freeze, infraction vl

        //Fails
        setItem(38, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.FIREBALL);
                ib.setName(ChatColor.GRAY + "Total Fails: " + ChatColor.AQUA + check.getFails().size());
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "Total amount of fails for this check since last startup.");
                return ib.toItemStack();
            }
        });

        //Freeze
        setItem(40, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                final Material material = (check.isAutobanFreeze() ? Material.ICE : Material.FIREWORK_CHARGE);
                ItemBuilder ib = new ItemBuilder(material);
                ib.setName(ChatColor.GRAY + "Freeze: " + enabled(check.isAutobanFreeze()));
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "When auto-ban is enabled, a player will be banned upon receiving an infraction.");
                ib.addLoreLine(ChatColor.GOLD + "Click to " + (check.isAutobanFreeze() ? "disable" : "enable"));
                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setWillUpdate(true);
                CmdSettings.editSetting(event.getPlayer(), checkType, "freeze", "toggle", true);
            }
        }));

        //Infraction VL
        setItem(42, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                ItemBuilder ib = new ItemBuilder(Material.GOLDEN_APPLE);
                ib.setName(ChatColor.GRAY + "Infraction VL: " + ChatColor.AQUA + check.getInfractionVL());
                ib.addLoreLine(" ");
                ib.addLoreLine(ChatColor.DARK_GRAY + "The amount of violations required to create an infraction.");
                ib.addLoreLine(ChatColor.GOLD + "LEFT Click to increase (+1)");
                ib.addLoreLine(ChatColor.GOLD + "RIGHT Click to decrease (-1)");
                return ib.toItemStack();
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setAcceptedClickTypes(ClickType.LEFT, ClickType.RIGHT);
                event.setWillUpdate(true);
                if (event.getClickType().equals(ClickType.LEFT)) {
                    check.setInfractionVL(check.getInfractionVL() + 1);
                } else if (event.getClickType().equals(ClickType.RIGHT)) {
                    check.setInfractionVL(check.getInfractionVL() - 1);
                    if (check.getInfractionVL() <= 0) {
                        check.setInfractionVL(1);
                    }
                }
            }
        }));

        setItem(53, new CloseItem());
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if (parent != null) {
            setItem(45, new BackItem());
        }
    }

    private String enabled(boolean enabled) {
        return textColorEnabled(textEnabled(enabled), enabled);
    }

    private String textEnabled(boolean enabled) {
        return enabled ? "Yes" : "No";
    }

    private String textColorEnabled(String text, boolean enabled) {
        return (enabled ? ChatColor.GREEN : ChatColor.RED) + text;
    }


}