/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.oldchecks;

public class CheckHeadRoll {

    /*

    public CheckHeadRoll() {
        Reflex.getInstance().getProtocolManager().addPacketListener(new PacketAdapter(Reflex.getInstance(), ListenerPriority.HIGHEST,
                PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.isCancelled()) return;
                Player p = event.getPlayer();
                ReflexPlayer ap = ReflexCache.get().getReflexPlayer(p);
                if (event.getPacketType() == PacketType.Play.Client.LOOK) {
                    float pitch = event.getPacket().getFloat().readSafely(1);
                    if (pitch > 90.1F || pitch < -90.1F) {
                       // if (fail(ap).isCancelled()) {
                      //      event.setCancelled(true);
                        }
                    }
                }
            }
        });
    }

*/

}
