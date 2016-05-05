package com.shawckz.reflex.checks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.Check;
import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;

import org.bukkit.entity.Player;

public class CheckTabComplete extends Check {

    public CheckTabComplete() {
        super(CheckType.TAB_COMPLETE);

        Reflex.getProtocolManager().addPacketListener(new PacketAdapter(Reflex.getPlugin(), ListenerPriority.NORMAL,
                PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if(!isEnabled()) return;
                Player p = event.getPlayer();
                ReflexPlayer ap = ReflexCache.get().getAresPlayer(p);
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    String s = event.getPacket().getStrings().read(0);
                    if (s.length() <= 2) return;
                    if(s.startsWith(".") && !s.startsWith("./")){
                        fail(ap, s);
                    }
                }
            }
        });
    }

}
