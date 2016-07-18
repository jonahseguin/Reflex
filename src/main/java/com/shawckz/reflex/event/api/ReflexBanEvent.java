/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.ban.ReflexBan;

public class ReflexBanEvent extends ReflexAPIEvent {

    private final ReflexBan ban;

    public ReflexBanEvent(ReflexBan ban) {
        this.ban = ban;
    }

    public ReflexBan getBan() {
        return ban;
    }

}
