/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.menu;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.backend.database.mongo.AutoMongo;
import com.shawckz.reflex.check.base.RViolation;
import com.shawckz.reflex.player.reflex.ReflexPlayer;
import com.shawckz.reflex.util.obj.ItemBuilder;
import com.shawckz.reflex.util.obj.TimeUtil;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.BackItem;
import ninja.amp.ampmenus.items.CloseItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bson.Document;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShowViolationsMenu extends ItemMenu {

    private final ReflexPlayer player;

    public ShowViolationsMenu(ReflexPlayer player) {
        super("Reflex - " + player.getName(), Size.SIX_LINE, Reflex.getInstance());
        this.player = player;

        List<AutoMongo> mongos = RViolation.select(new Document("uniqueId", player.getUniqueId()), RViolation.class);

        int i = 0;

        final ShowViolationsMenu thisMenu = this;

        for (AutoMongo mongo : mongos) {
            if (mongo instanceof RViolation) {
                RViolation vl = (RViolation) mongo;
                setItem(i, new RDynMenuItem() {
                    @Override
                    public ItemStack getFinalIcon(Player viewer) {
                        ItemBuilder itemBuilder = new ItemBuilder(Material.PAPER);

                        itemBuilder.setName(ChatColor.BLUE + "#" + vl.getId());

                        itemBuilder.addLoreLine(" ");
                        itemBuilder.addLoreLine(ChatColor.DARK_GRAY + "Check: " + vl.getCheckType());
                        itemBuilder.addLoreLine(ChatColor.DARK_GRAY + "Source: " + vl.getSource());
                        itemBuilder.addLoreLine(ChatColor.DARK_GRAY + TimeUtil.format(vl.getTime()));

                        return itemBuilder.toItemStack();
                    }
                }.action(new RMenuHandler() {
                    @Override
                    public void onClick(ItemClickEvent event) {
                        LookupViolationMenu lookupViolationMenu = new LookupViolationMenu(player, vl);
                        lookupViolationMenu.setParent(thisMenu);
                        lookupViolationMenu.open(event.getPlayer());
                    }
                }));
                i++;
                if (i >= 53) {
                    break;
                }
            }
        }


        setItem(53, new CloseItem());
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if (parent != null) {
            setItem(53, new BackItem());
        }
    }


}
