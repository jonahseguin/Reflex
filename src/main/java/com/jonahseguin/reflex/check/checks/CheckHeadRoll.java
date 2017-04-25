/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.check.checks;

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
 * Created by Jonah Seguin on Mon 2017-04-24 at 20:32.
 * Project: Reflex
 *
 * Check: HeadRoll
 *
 * 'Headless' hacks; when a player's head turns 'further' than possible
 */
public class CheckHeadRoll extends Check {

    public CheckHeadRoll(Reflex instance) {
        super(instance, CheckType.HEAD_ROLL);

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
}
