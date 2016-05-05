package com.shawckz.reflex.cmd.commands;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.cmd.CmdArgs;
import com.shawckz.reflex.cmd.Command;
import com.shawckz.reflex.cmd.ReflexCommand;
import com.shawckz.reflex.player.ReflexCache;
import com.shawckz.reflex.player.ReflexPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command(name = "alerts", usage = "/alerts", playerOnly = true, permission = "reflex.alerts")
public class CommandAlerts implements ReflexCommand {

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        ReflexPlayer ap = ReflexCache.get().getAresPlayer(p);
        if(cmdArgs.getArgs().length > 0 && p.hasPermission("ares.alerts.others")){
            Player t = cmdArgs.getPlayer(0);
            if(t != null){
                ReflexPlayer at = ReflexCache.get().getAresPlayer(t);
                at.setAlertsEnabled(!at.isAlertsEnabled());
                at.msg(Reflex.getPrefix() + " Alerts have been " + (at.isAlertsEnabled() ?
                        "&aenabled" : "&cdisabled") + "&7.");
                p.sendMessage(Reflex.getPrefix() + " &7Alerts have been "+(at.isAlertsEnabled() ? "&aenabled" : "&cdisabled")+
                        "&7 for "+t.getDisplayName()+".");
            }
            else{
                p.sendMessage(ChatColor.RED+"Player '"+cmdArgs.getArg(0)+"' not found.");
            }
        }
        else{
            ap.setAlertsEnabled(!ap.isAlertsEnabled());
            p.sendMessage(Reflex.getPrefix() + " &7Alerts have been "+(ap.isAlertsEnabled() ?
                    "&aenabled" : "&cdisabled")+"&7.");
        }
    }
}
