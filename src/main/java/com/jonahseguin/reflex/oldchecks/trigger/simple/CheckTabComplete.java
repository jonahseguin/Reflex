/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.oldchecks.trigger.simple;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.oldchecks.base.RCheckType;
import com.jonahseguin.reflex.oldchecks.trigger.RTrigger;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import org.bukkit.entity.Player;

public class CheckTabComplete extends RTrigger {

    public CheckTabComplete(Reflex instance) {
        super(instance, CheckType.TAB_COMPLETE, RCheckType.TRIGGER);

        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL,
                PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!isEnabled()) return;
                Player p = event.getPlayer();
                ReflexPlayer ap = getPlayer(p);
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    String s = event.getPacket().getStrings().read(0);
                    if (s.length() <= 2) return;
                    if (s.startsWith(".") && !s.startsWith("./")) {
                        if (fail(ap, s).isCancelled()) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getCaptureTime() {
        return -1;
    }

}
