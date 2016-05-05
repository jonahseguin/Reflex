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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class CheckBedFly extends Check {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnterBed(PlayerBedEnterEvent e){
        if(e.isCancelled()) return;
        Player p = e.getPlayer();
        ReflexPlayer ap = ReflexCache.get().getAresPlayer(p);
        ap.getData().setEnteredBed(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerBedLeaveEvent e){
        Player p = e.getPlayer();
        ReflexPlayer ap = ReflexCache.get().getAresPlayer(p);
        ap.getData().setEnteredBed(false);
    }

    public CheckBedFly() {
        super(CheckType.BED_FLY);

        Reflex.getProtocolManager().addPacketListener(new PacketAdapter(Reflex.getPlugin(), ListenerPriority.HIGHEST,
                PacketType.Play.Client.ENTITY_ACTION) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!isEnabled()) return;
                if (event.isCancelled()) return;
                Player p = event.getPlayer();
                ReflexPlayer ap = ReflexCache.get().getAresPlayer(p);
                if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
                    int i = event.getPacket().getIntegers().readSafely(1);//The ID of the action
                    if(i == 3){//Entity Action ID 2 = Leave Bed
                        if(!ap.getData().isEnteredBed()) {
                            if (fail(ap).isCancelled()) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        });

    }

}
