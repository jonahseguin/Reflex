/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.event.api;

import com.shawckz.reflex.ban.ReflexBan;

public class ReflexConfirmBanEvent extends ReflexAPIEvent {

    private final ReflexBan ban;
    private boolean result;

    public ReflexConfirmBanEvent(ReflexBan ban, boolean result) {
        this.ban = ban;
        this.result = result;
    }

    public ReflexBan getBan() {
        return ban;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
