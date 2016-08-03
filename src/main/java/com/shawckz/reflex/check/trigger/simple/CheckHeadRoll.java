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

public class CheckHeadRoll extends RTrigger {

    public CheckHeadRoll(Reflex instance) {
        super(instance, CheckType.HEAD_ROLL, RCheckType.TRIGGER);

        instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.HIGHEST,
                PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!isEnabled()) return;
                Player p = event.getPlayer();
                ReflexPlayer ap = getPlayer(p); //Use new handy local method that gets a player from the cache, via the provided instance
                if (event.getPacketType() == PacketType.Play.Client.LOOK) {
                    float pitch = event.getPacket().getFloat().readSafely(1);
                    if (pitch > 90.1F || pitch < -90.1F) { //.1 because sometimes head can glitch when spinning fast
                        if (fail(ap).isCancelled()) {
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
