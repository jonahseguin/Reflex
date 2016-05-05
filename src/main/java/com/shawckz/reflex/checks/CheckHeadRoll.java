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

public class CheckHeadRoll extends Check {

    public CheckHeadRoll() {
        super(CheckType.HEAD_ROLL);
        Reflex.getProtocolManager().addPacketListener(new PacketAdapter(Reflex.getPlugin(), ListenerPriority.HIGHEST,
                PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if(!isEnabled()) return;
                if(event.isCancelled()) return;
                Player p = event.getPlayer();
                ReflexPlayer ap = ReflexCache.get().getAresPlayer(p);
                if (event.getPacketType() == PacketType.Play.Client.LOOK) {
                    float pitch = event.getPacket().getFloat().readSafely(1);
                    if (pitch > 90.1F || pitch < -90.1F) {
                        if(fail(ap).isCancelled()){
                            event.setCancelled(true);
                        }
                    }
                }
            }
        });
    }


}
