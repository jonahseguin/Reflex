/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu.other;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.ban.ReflexBan;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.menu.backend.RDynMenuItem;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.menu.items.BackItem;
import com.jonahseguin.reflex.util.menu.items.CloseItem;
import com.jonahseguin.reflex.util.menu.items.StaticMenuItem;
import com.jonahseguin.reflex.util.menu.menus.ItemMenu;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import com.jonahseguin.reflex.util.obj.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerMenu extends ItemMenu {

    private final ReflexPlayer player;

    public PlayerMenu(ReflexPlayer player) {
        super("Reflex - " + player.getName(), Size.ONE_LINE, Reflex.getInstance());
        this.player = player;

        final PlayerMenu thisMenu = this;

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
                ib.addLoreLine(" ");
                for (CheckType checkType : CheckType.values()) {
                    ib.addLoreLine(ChatColor.DARK_GRAY + checkType.getName() + ": " + ChatColor.GRAY + player.getRecord().getViolationCount(checkType));
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
                if (Reflex.getInstance().getBanManager().isBanned(player.getUniqueId())) {
                    ReflexBan ban = Reflex.getInstance().getBanManager().getBan(player.getUniqueId());
                    ib.addLoreLine(ChatColor.RED + "Player has active ban");
                    ib.addLoreLine(ChatColor.DARK_GRAY + "Check: " + ban.getCheckType());
                    ib.addLoreLine(ChatColor.DARK_GRAY + "VL: " + ban.getViolationCount());
                    ib.addLoreLine(ChatColor.DARK_GRAY + "Time: " + TimeUtil.format(ban.getTime()));
                    ib.addLoreLine(ChatColor.DARK_GRAY + "Expires: " + TimeUtil.format(ban.getExpiration()));
                    ib.addLoreLine(ChatColor.GOLD + "Confirmed: " + (ban.isConfirmed() ? ChatColor.GREEN + "YES" : ChatColor.RED + "NO"));
                    if (ban.isConfirmed()) {
                        ib.addLoreLine(ChatColor.DARK_GRAY + "Banned Correctly: " + ban.isBannedCorrectly());
                    }
                } else {
                    ib.addLoreLine(ChatColor.DARK_GRAY + "Not banned.");
                }

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
