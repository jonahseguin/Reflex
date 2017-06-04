/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.menu.note;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.menu.backend.RDynMenuItem;
import com.jonahseguin.reflex.util.menu.items.BackItem;
import com.jonahseguin.reflex.util.menu.items.CloseItem;
import com.jonahseguin.reflex.util.menu.menus.ItemMenu;
import com.jonahseguin.reflex.util.obj.ItemBuilder;
import com.jonahseguin.reflex.util.obj.Note;
import com.jonahseguin.reflex.util.obj.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * Created by Jonah Seguin on Mon 2017-05-29 at 14:36.
 * Project: Reflex
 */
public class NotesMenu extends ItemMenu {

    private NotesMenu() {
        super("Reflex - Notes", Size.SIX_LINE, Reflex.getInstance());
    }

    public NotesMenu(Set<Note> notes) {
        this();
        // All violations for player

        int posn = 0;
        for (Note note : notes) {
            addNoteItem(posn, note);
            posn++;
        }

        setItem(53, new CloseItem());
    }

    private void addNoteItem(int posn, Note note) {
        setItem(posn, new RDynMenuItem() {
            @Override
            public ItemStack getFinalIcon(Player viewer) {
                return getNoteItem(note);
            }
        });
    }

    private ItemStack getNoteItem(Note note) {
        ItemBuilder ib = new ItemBuilder(Material.PAPER);
        ib.setName(ChatColor.GOLD + "#" + note.getId().substring(0, 5));
        ib.addLoreLine(ChatColor.DARK_GRAY + "ID: " + note.getId());
        ib.addLoreLine(" ");
        ib.addLoreLine(ChatColor.GRAY + "Note: " + ChatColor.AQUA + note.getNote());
        ib.addLoreLine(ChatColor.GRAY + "Time: " + ChatColor.AQUA + TimeUtil.format(note.getTime()));
        ib.addLoreLine(ChatColor.GRAY + "Player: " + ChatColor.AQUA + note.getPlayer().getName());
        ib.addLoreLine(ChatColor.GRAY + "Author: " + ChatColor.AQUA + note.getAuthor().getName());
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
