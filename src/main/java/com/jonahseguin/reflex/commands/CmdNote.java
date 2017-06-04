/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.commands;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.command.RCmd;
import com.jonahseguin.reflex.backend.command.RCmdArgs;
import com.jonahseguin.reflex.backend.command.RCommand;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.backend.database.mongo.AutoMongo;
import com.jonahseguin.reflex.check.alert.AlertManager;
import com.jonahseguin.reflex.menu.note.NotesMenu;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.obj.Note;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Jonah Seguin on Sun 2017-05-28 at 12:38.
 * Project: Reflex
 */
public class CmdNote implements RCommand {

    @RCmd(name = "reflex note", aliases = {"! note", "rx note"}, usage = "/reflex note <player> <note>",
            description = "Add a note to a player", permission = ReflexPerm.NOTE_ADD, minArgs = 2, playerOnly = true)
    public void onCmdNoteAdd(final RCmdArgs args) {
        final Player sender = args.getSender().getPlayer();
        final String targetName = args.getArg(0);
        final String noteString = args.getBuiltArgs(1);

        Reflex.getScheduler().asyncTask(() -> {
            ReflexPlayer author = Reflex.getPlayer(sender);
            ReflexPlayer target = Reflex.getPlayer(targetName);
            if (target != null) {
                Note note = new Note(target, author, noteString);
                note.update();

                sender.sendMessage(ChatColor.GRAY + "Note added.");
                AlertManager.staffMsg(RLang.format(ReflexLang.ALERT_PREFIX) + RLang.format(ReflexLang.NOTE_ADD, author.getName(), target.getName(), noteString));
            } else {
                RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND_DATABASE, targetName);
            }

        });
    }

    @RCmd(name = "reflex notes", aliases = {"! notes", "rx notes"}, usage = "/reflex notes <player>",
            description = "View notes for a player", permission = ReflexPerm.NOTE_VIEW, minArgs = 1, playerOnly = true)
    public void onCmdNotes(final RCmdArgs args) {
        final Player sender = args.getSender().getPlayer();
        final String targetName = args.getArg(0);

        Reflex.getScheduler().asyncTask(() -> {
            ReflexPlayer target = Reflex.getPlayer(targetName);
            if (target != null) {
                List<AutoMongo> mongos = Note.select(new Document("player", target.getUniqueId()), Note.class);
                Set<Note> notes = mongos.stream().filter(mongo -> mongo instanceof Note).map(mongo -> (Note) mongo).collect(Collectors.toSet());
                if (!notes.isEmpty()) {
                    NotesMenu notesMenu = new NotesMenu(notes);
                    notesMenu.open(sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "No notes to display for this player.");
                }
            } else {
                RLang.send(sender, ReflexLang.PLAYER_NOT_FOUND_DATABASE, targetName);
            }
        });
    }

}
