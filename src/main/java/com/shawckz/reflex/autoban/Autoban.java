package com.shawckz.reflex.autoban;

import com.shawckz.reflex.check.CheckType;
import lombok.Data;

@Data
public class Autoban {
    private final String name;
    private final CheckType checkType;
    private boolean cancelled;
}
