package com.shawckz.reflex.checks;

import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.check.TimerCheck;
import com.shawckz.reflex.configuration.annotations.ConfigData;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CheckSpeed extends TimerCheck {

    @ConfigData("max-blocks-per-second")
    @Getter @Setter private double maxBlocksPerSecond = 20;

    public CheckSpeed() {
        super(CheckType.SPEED);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent e){
        if(e.isCancelled()) return;
        Player p = e.getPlayer();
        ReflexPlayer ap = ReflexCache.get().getAresPlayer(p);
        double distance = e.getTo().distance(e.getFrom());
        ap.getData().setBlocksPerSecond(ap.getData().getBlocksPerSecond() + distance);
    }

    @Override
    public final void check(ReflexPlayer player) {
        final Player p = player.getBukkitPlayer();
        if(p.isInsideVehicle()) return;// Riding a horse or minecart makes it possible to move much faster
        if(p.getAllowFlight()) return;
        //If they have both speed AND slowness, we're just going to ignore them...

        if(p.hasPotionEffect(PotionEffectType.SPEED) && !p.hasPotionEffect(PotionEffectType.SLOW)){
            PotionEffect potionEffect = null;

            for(PotionEffect effect : p.getActivePotionEffects()){
                if(effect.getType().equals(PotionEffectType.SPEED)){
                    potionEffect = effect;
                    break;
                }
            }

            double maxBps = this.maxBlocksPerSecond;

            if(potionEffect != null){
                //They have speed
                int amp = potionEffect.getAmplifier();
                maxBps = maxBlocksPerSecond + (maxBlocksPerSecond*((amp + 1)*0.20));// Speed increases by 20%
            }

            double bps = player.getData().getBlocksPerSecond();

            if(bps > maxBps){
                fail(player);
            }

        }
        else if (p.hasPotionEffect(PotionEffectType.SLOW) && !p.hasPotionEffect(PotionEffectType.SPEED)){
            //Slowness decreases by 15% * potion level
            PotionEffect potionEffect = null;
            for(PotionEffect effect : p.getActivePotionEffects()){
                if(effect.getType().equals(PotionEffectType.SLOW)){
                    potionEffect = effect;
                    break;
                }
            }

            double maxBps = this.maxBlocksPerSecond;

            if(potionEffect != null){
                //They have slow
                int amp = potionEffect.getAmplifier();
                maxBps = maxBlocksPerSecond * (amp - (amp * 0.15));// Slow decreases by 15%
            }

            double bps = player.getData().getBlocksPerSecond();

            if(bps > maxBps){
                fail(player);
            }
        }
        else if (!p.hasPotionEffect(PotionEffectType.SPEED) && !p.hasPotionEffect(PotionEffectType.SLOW)){

            double bps = player.getData().getBlocksPerSecond();

            if(bps > this.maxBlocksPerSecond){
                fail(player);
            }
        }

        //Reset their blocks per second
        player.getData().setBlocksPerSecond(0);
    }

    @Override
    public final void cancel(ReflexPlayer player) {
        player.getBukkitPlayer().teleport(player.getBukkitPlayer().getLocation());
    }

}
