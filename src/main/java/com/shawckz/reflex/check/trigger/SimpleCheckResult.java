/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger;

import com.shawckz.reflex.check.base.RViolation;
import lombok.Data;


@Data
public class SimpleCheckResult {

    private final boolean cancelled;
    private final RViolation violation;

}
