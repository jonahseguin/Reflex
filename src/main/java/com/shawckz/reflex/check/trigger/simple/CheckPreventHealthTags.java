/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.trigger.simple;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.base.CheckType;
import com.shawckz.reflex.check.base.RCheckType;
import com.shawckz.reflex.check.trigger.RTrigger;

/**
 * Created by jonahseguin on 2016-07-26.
 */
public class CheckPreventHealthTags extends RTrigger {

    public CheckPreventHealthTags() {
        super(CheckType.HEALTH_TAGS, RCheckType.TRIGGER);

        Reflex.getInstance().getProtocolManager().addPacketListener(new PacketAdapter(Reflex.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Server.UPDATE_HEALTH) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!isEnabled()) return;
                event.setCancelled(true);
            }
        });
    }



    @Override
    public int getCaptureTime() {
        return -1;
    }
}
