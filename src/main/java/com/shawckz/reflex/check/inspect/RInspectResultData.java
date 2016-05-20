/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.inspect;

import lombok.Getter;

@Getter
public class RInspectResultData {

    private final RInspectResultType type;
    private final String detail;

    public RInspectResultData(RInspectResultType type) {
        this(type, null);
    }

    public RInspectResultData(RInspectResultType type, String detail) {
        this.type = type;
        this.detail = detail;
    }
}
