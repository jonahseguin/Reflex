/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.commands;

import com.jonahseguin.reflex.backend.command.RCmd;
import com.jonahseguin.reflex.backend.command.RCmdArgs;
import com.jonahseguin.reflex.backend.command.RCommand;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.backend.configuration.ReflexPerm;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.menu.check.CheckMenu;
import com.jonahseguin.reflex.menu.check.ChecksMenu;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on Tue 2017-05-23 at 13:35.
 * Project: Reflex
 */
public class CmdCheck implements RCommand {

    @RCmd(name = "reflex check", usage = "/reflex check <check>", description = "Manage a check's settings", permission = ReflexPerm.CHECK,
            playerOnly = true, minArgs = 1, aliases = {"! check", "rx check", "rflex check"})
    public void onCmdCheck(final RCmdArgs args) {
        final Player player = args.getSender().getPlayer();
        String checkName = args.getArg(0);
        CheckType checkType = CheckType.fromString(checkName);
        if (checkType != null) {
            CheckMenu checkMenu = new CheckMenu(checkType);
            checkMenu.open(player);
        } else {
            RLang.send(player, ReflexLang.CHECK_NOT_FOUND, checkName);
        }
    }

    @RCmd(name = "reflex checks", usage = "/reflex checks", description = "Manage all checks", permission = ReflexPerm.CHECKS,
            playerOnly = true, aliases = {"! checks", "rx checks", "rflex checks"})
    public void onCmdChecks(final RCmdArgs args) {
        final Player player = args.getSender().getPlayer();
        ChecksMenu checksMenu = new ChecksMenu();
        checksMenu.open(player);
    }

}
