package com.shawckz.reflex.prevent.check;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.bridge.RCheckType;
import com.shawckz.reflex.core.configuration.annotations.ConfigData;
import com.shawckz.reflex.core.player.ReflexPlayer;
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
                Bukkit.getServer().getPluginManager().registerEvents(this, Reflex.getInstance());
                Bukkit.getLogger().info("[Reflex] Registered events for "+getName());
            }
            else{
                HandlerList.unregisterAll(this);
            }
        }
        this.enabled = enabled;
    }

    protected final CheckViolation fail(ReflexPlayer player, String... detail){// detail passed to this method overrides #getDetail
        if(this.isCancel()){
            cancel(player);
        }

        String d = null;

        CheckViolation violation = new CheckViolation(player.getUniqueId(), player.getData().copy());

        if(detail != null && detail.length > 0) {
            d = detail[0];
        }
        else {
            String s = getDetail(violation);
            if(s != null) {
                d = s;
            }
        }

        player.addVL(this.getCheckType());

        Alert alert = new Alert(player, RCheckType.CHECK, this.getCheckType(), violation, player.getVL(this.getCheckType()));

        if(d != null) {
            alert.setDetail(d);
        }

        alert.sendAlert();

        if(this.getCheckType().isTrigger()) {

        }


        return violation;
    }

    public void cancel(ReflexPlayer player) {
        // Can be overridden
    }

    public final String getName(){
        return checkType.getName();
    }

    public String getDetail(CheckViolation violation){
        // Can be overriden
        return null;
    }

}
