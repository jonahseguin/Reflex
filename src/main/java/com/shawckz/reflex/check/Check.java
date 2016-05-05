package com.shawckz.reflex.check;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.autoban.AutobanManager;
import com.shawckz.reflex.configuration.annotations.ConfigData;
import com.shawckz.reflex.player.ReflexPlayer;
import com.shawckz.reflex.util.Alert;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * The Check class
 * Superclass to all Checks, provides base and other important stuff.
 */
@Getter
@Setter
public abstract class Check extends CheckConfig implements Listener {

    private final CheckType checkType;

    @ConfigData(value = "enabled") private boolean enabled = true;

    @ConfigData(value = "autoban") private boolean autoban = true;

    @ConfigData(value = "cancel") private boolean cancel = true;

    @ConfigData(value = "punish-level") private int punishLevel = 10;

    @ConfigData(value = "raise-level") private int raiseLevel = 2;

    @ConfigData(value = "punish-command") private String punishCommand = "";

    public Check(final CheckType checkType) {
        super(checkType);
        this.checkType = checkType;
    }

    public final void setEnabled(final boolean enabled) {
        if(this.enabled != enabled){
            if(enabled){
                Bukkit.getServer().getPluginManager().registerEvents(this, Reflex.getPlugin());
                Bukkit.getLogger().info("[Reflex] Registered events for "+getName());
            }
            else{
                HandlerList.unregisterAll(this);
            }
        }
        this.enabled = enabled;
    }

    protected final Violation fail(ReflexPlayer player, String... detail){
        if(this.isCancel()){
            cancel(player);
        }

        final Violation violation = player.addVL(this.getCheckType(),this.isCancel());

        if(violation.getVl() > this.getPunishLevel() && this.isAutoban() && !AutobanManager.hasAutoban(player.getName())){
            //Inspect the player



        }
        else{
            if(getDetail(violation) != null){
                // Send detailed alert
                Alert.send(violation, getDetail(violation));
            }
            else if (detail != null && detail.length > 0){
                Alert.send(violation, detail[0]);
            }
            else{
                // Send normal alert
                Alert.send(violation);
            }
        }

        return violation;
    }

    public void cancel(ReflexPlayer player) {
        // Can be overridden
    }

    public final String getName(){
        return checkType.getName();
    }

    public String getDetail(Violation violation){
        // Can be overriden
        return null;
    }

}
