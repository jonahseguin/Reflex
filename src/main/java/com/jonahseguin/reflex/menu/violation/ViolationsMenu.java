/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu.violation;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.check.alert.CheckAlert;
import com.jonahseguin.reflex.check.alert.GroupedAlert;
import com.jonahseguin.reflex.check.violation.CheckViolation;
import com.jonahseguin.reflex.menu.backend.RDynMenuItem;
import com.jonahseguin.reflex.menu.backend.RMenuHandler;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;
import com.jonahseguin.reflex.util.menu.items.BackItem;
import com.jonahseguin.reflex.util.menu.items.CloseItem;
import com.jonahseguin.reflex.util.menu.menus.ItemMenu;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import com.jonahseguin.reflex.util.obj.TimeUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jonah Seguin on Tue 2017-05-23 at 20:41.
 * Project: Reflex
 */
public class ViolationsMenu extends ItemMenu {

    private ViolationsMenu() {
        super("Reflex - Violations", Size.SIX_LINE, Reflex.getInstance());
    }

    public ViolationsMenu(ReflexPlayer player) {
        this();
        // All violations for player

        int posn = 0;
        for (CheckViolation violation : player.getRecord().getAllViolations()) {
            addViolationItem(posn, violation, player);
            posn++;
        }

        setItem(53, new CloseItem());
    }

    public ViolationsMenu(ReflexPlayer player, CheckType checkType) {
        this();
        // Violations for checkType for player

        int posn = 0;
        for (CheckViolation violation : player.getRecord().getAllViolations(checkType)) {
            addViolationItem(posn, violation, player);
            posn++;
        }

        setItem(53, new CloseItem());
    }

    public ViolationsMenu(GroupedAlert groupedAlert) {
        this();
        // Violations in alert

        int posn = 0;
        for (CheckAlert alert : groupedAlert.getAlertSet().getAlerts()) {
            CheckViolation violation = alert.getViolation();
            addViolationItem(posn, violation, alert.getReflexPlayer());
            posn++;
        }

        setItem(53, new CloseItem());
    }

    private void addViolationItem(int posn, CheckViolation violation, ReflexPlayer player) {
        setItem(posn, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                return getViolationItem(violation);
            }
        }.action(new RMenuHandler() {
            @Override
            public void onClick(ItemClickEvent event) {
                event.setWillClose(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Reflex.getInstance(), () -> {
                    if (event.getPlayer() != null && event.getPlayer().isOnline()) {
                        ViolationMenu violationMenu = new ViolationMenu(player, violation);
                        violationMenu.setParent(ViolationsMenu.this);
                        violationMenu.open(event.getPlayer());
                    }
                }, 3);
            }
        }));
    }

    private ItemStack getViolationItem(CheckViolation violation) {
        ItemBuilder ib = new ItemBuilder(Material.PAPER);
        ib.setName(ChatColor.GOLD + violation.getCheckType().getName());
        ib.addLoreLine(ChatColor.DARK_GRAY + "ID: " + violation.getId());
        ib.addLoreLine((violation.isValid() ? ChatColor.GREEN + "VALID" : ChatColor.RED + "INVALID"));
        ib.addLoreLine(" ");
        ib.addLoreLine(ChatColor.GRAY + "Time: " + ChatColor.AQUA + TimeUtil.format(violation.getTime()));
        ib.addLoreLine(ChatColor.GRAY + "Expires: " + ChatColor.AQUA + TimeUtil.format(violation.getExpiryTime()));
        ib.addLoreLine(ChatColor.GRAY + "Detail: " + ChatColor.AQUA + violation.getDetail());
        ib.addLoreLine(ChatColor.GRAY + "VL: " + ChatColor.AQUA + violation.getVl());
        return ib.toItemStack();
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if (parent != null) {
            setItem(52, new BackItem());
        }
    }

}
