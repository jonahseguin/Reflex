/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.ban;

import java.util.HashSet;
import java.util.Set;

public class BanCache {

    private final Set<ReflexBan> bans = new HashSet<>();

    public Set<ReflexBan> getBans() {
        return bans;
    }

    public boolean hasBan(ReflexBan ban) {
        return bans.contains(ban);
    }

    public void add(ReflexBan ban) {
        if (!bans.contains(ban)) {
            boolean contains = false;
            for (ReflexBan b : bans) {
                if (b.getId().equalsIgnoreCase(ban.getId())) {
                    contains = true;
                }
            }
            if (!contains) {
                bans.add(ban);
            }
        }
    }

    public void remove(ReflexBan ban) {
        bans.remove(ban);
    }

    public boolean hasActiveBan() {
        for (ReflexBan ban : bans) {
            if (ban.isActive()) {
                return true;
            }
        }
        return false;
    }

    public ReflexBan getActiveBan() {
        for (ReflexBan ban : bans) {
            if (ban.isActive()) {
                return ban;
            }
        }
        return null;
    }

}
