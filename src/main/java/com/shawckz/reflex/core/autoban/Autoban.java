package com.shawckz.reflex.core.autoban;

import com.shawckz.reflex.bridge.CheckType;
import lombok.Data;

@Data
public class Autoban {
    private final String name;
    private final CheckType checkType;
    private boolean cancelled;
}
