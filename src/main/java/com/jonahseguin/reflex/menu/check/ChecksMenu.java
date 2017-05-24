/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu.check;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.menu.backend.RDynMenuItem;
import com.jonahseguin.reflex.menu.backend.RMenuHandler;
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
 * Created by Jonah Seguin on Mon 2017-05-22 at 20:27.
 * Project: Reflex
 */
public class ChecksMenu extends ItemMenu {

    public ChecksMenu() {
        super("Reflex - Checks", Size.THREE_LINE, Reflex.getInstance());

        int posn = 0;

        for (Check check : Reflex.getInstance().getCheckManager().getChecks()) {
            setItem(posn, new RDynMenuItem() {
                @Override
                public ItemStack getFinalIcon(Player viewer) {
                    ItemBuilder ib = new ItemBuilder(Material.PAPER);
                    ib.setName(ChatColor.GOLD + check.getName());
                    ib.addLoreLine(" ");
                    ib.addLoreLine(ChatColor.GRAY + "Status: " + (check.isEnabled() ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED"));
                    ib.addLoreLine(ChatColor.GRAY + "Auto-Ban: " + (check.isAutoban() ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED"));
                    ib.addLoreLine(ChatColor.GRAY + "Auto-Ban Freeze: " + (check.isAutobanFreeze() ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED"));
                    ib.addLoreLine(ChatColor.GRAY + "Cancelling: " + (check.isCancel() ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED"));
                    ib.addLoreLine(ChatColor.GRAY + "Infraction VL: " + ChatColor.AQUA + check.getInfractionVL());

                    return ib.toItemStack();
                }
            }.action(new RMenuHandler() {
                @Override
                public void onClick(final ItemClickEvent event) {
                    event.setWillClose(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Reflex.getInstance(), () -> {
                        if (event.getPlayer() != null && event.getPlayer().isOnline()) {
                            CheckMenu checkMenu = new CheckMenu(check.getCheckType());
                            checkMenu.open(event.getPlayer());
                        }
                    }, 3);
                }
            }));

            posn++;
        }


        setItem(26, new CloseItem());
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if (parent != null) {
            setItem(25, new BackItem());
        }
    }

}
