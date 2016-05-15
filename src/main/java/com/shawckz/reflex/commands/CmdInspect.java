/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.commands;

import com.shawckz.reflex.backend.command.RCmd;
import com.shawckz.reflex.backend.command.RCmdArgs;
import com.shawckz.reflex.backend.command.RCommand;

public class CmdInspect implements RCommand {

    @RCmd(name = "reflex inspect", usage = "/reflex inspect <player> <check> <seconds>", minArgs = 3, permission = "reflex.use", aliases = {"! inspect", "reflex inspect", "rx inspect", "rflex inspect"})
    public void onCmdInspect(RCmdArgs args) {

    }

}
