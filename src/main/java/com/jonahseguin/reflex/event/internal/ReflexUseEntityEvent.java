/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.event.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class ReflexUseEntityEvent extends ReflexInternalEvent {

    private final Player player;
    private final boolean cancelled;


}
