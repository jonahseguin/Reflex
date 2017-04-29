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
