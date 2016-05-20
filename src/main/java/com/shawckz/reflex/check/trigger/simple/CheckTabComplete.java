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
import com.shawckz.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;

public class CheckTabComplete extends RTrigger {



    public CheckTabComplete() {
        super(CheckType.TAB_COMPLETE, RCheckType.TRIGGER);

        Reflex.getInstance().getProtocolManager().addPacketListener(new PacketAdapter(Reflex.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!isEnabled()) return;
                Player p = event.getPlayer();
                ReflexPlayer ap = Reflex.getInstance().getCache().getReflexPlayer(p);
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    String s = event.getPacket().getStrings().read(0);
                    if (s.length() <= 2) return;
                    if (s.startsWith(".") && !s.startsWith("./")) {
                        fail(ap, s);
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
