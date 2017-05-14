/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.check.violation.CheckViolation;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import com.jonahseguin.reflex.util.obj.TimeUtil;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.BackItem;
import ninja.amp.ampmenus.items.CloseItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ShowViolationsMenu extends ItemMenu {

    private final ReflexPlayer player;
    private Map<Integer, Set<CheckViolation>> pages = new ConcurrentHashMap<>();


    public ShowViolationsMenu(ReflexPlayer player) {
        super("Reflex - " + player.getName(), Size.SIX_LINE, Reflex.getInstance());
        this.player = player;

        List<AutoMongo> mongos = RViolation.select(new Document("uniqueId", player.getUniqueId()), RViolation.class);

        int i = 0;

        final ShowViolationsMenu thisMenu = this;


        setItem(53, new CloseItem());
    }

    public void load() {
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
                        ViolationMenu lookupViolationMenu = new ViolationMenu(player, vl);
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
    }

    @Override
    public void setParent(ItemMenu parent) {
        super.setParent(parent);
        if (parent != null) {
            setItem(53, new BackItem());
        }
    }


}
