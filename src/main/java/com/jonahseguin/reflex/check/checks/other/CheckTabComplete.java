/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks.other;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.Check;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;

import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on Sat 2017-04-29 at 17:00.
 * Project: Reflex
 */
public class CheckTabComplete extends Check {

    public CheckTabComplete(Reflex reflex) {
        super(reflex, CheckType.TAB_COMPLETE);

        getReflex().getProtocolManager().addPacketListener(new PacketAdapter(getReflex(), ListenerPriority.NORMAL,
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
                        fail(ap, s).cancelIfAllowed(event);
                    }
                }
            }
        });

    }

    @Override
    public String description() {
        return "Detects when a player tab completes a client command starting with '.'";
    }
}
