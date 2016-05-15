/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.oldchecks;

public class CheckTabComplete {

    /*

    public CheckTabComplete() {
        super(CheckType.TAB_COMPLETE);

        Reflex.getProtocolManager().addPacketListener(new PacketAdapter(Reflex.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!isEnabled()) return;
                Player p = event.getPlayer();
                ReflexPlayer ap = ReflexCache.get().getReflexPlayer(p);
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

*/

}
