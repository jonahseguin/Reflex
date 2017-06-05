/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.ban;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.RLang;
import com.jonahseguin.reflex.backend.configuration.ReflexLang;
import com.jonahseguin.reflex.check.CheckType;
import com.jonahseguin.reflex.check.alert.AlertManager;
import com.jonahseguin.reflex.check.violation.Infraction;
import com.jonahseguin.reflex.event.api.ReflexBanEvent;
import com.jonahseguin.reflex.player.reflex.ReflexPlayer;
import com.jonahseguin.reflex.util.exception.ReflexRuntimeException;
import com.jonahseguin.reflex.util.obj.AutobanMethod;
import com.jonahseguin.reflex.util.obj.Freeze;
import lombok.Getter;
import lombok.Setter;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;

@Getter
@Setter
/**
 * Reflex AutoBan
 * Automatically ban a player after X seconds via the set method in the config (CONSOLE COMMAND or REFLEX BAN)
 * Can be cancelled using the Reflex Cancel command by anyone with the permission (staff..)
 */
public class Autoban {

    private ReflexPlayer player;
    private int cd;
    private boolean cancelled;
    private boolean executed = false;
    private CheckType check;
    private Infraction infraction;

    public Autoban(ReflexPlayer player, int cd, CheckType check, Infraction infraction) {
        this.player = player;
        this.cd = cd;
        this.check = check;
        this.cancelled = false;
        this.infraction = infraction;
    }

    /**
     * Start the AutoBan countdown
     */
    public void run() {

        if (player.getBukkitPlayer() == null) return;

        if (player.getBukkitPlayer() != null) {
            if (Reflex.getInstance().getCheckManager().getCheck(check).isAutobanFreeze()) {
                Freeze freeze = new Freeze(player.getBukkitPlayer());
                freeze.run();
                player.msg(ReflexLang.AUTOBAN_CHEATER, check.getName());
            }
        }

        FancyMessage fm = new FancyMessage(RLang.format(ReflexLang.ALERT_PREFIX));
        fm.then(RLang.format(ReflexLang.AUTOBAN, player.getName(), check.getName(), cd + ""))
                .tooltip(ChatColor.YELLOW + "Click here to cancel ban on " + player.getName())
                .command("/reflex cancel " + player.getName());
        AlertManager.staffMsg(fm);


        new BukkitRunnable() {

            @Override
            public void run() {

                if (cancelled || executed) {
                    cancel();
                    return;
                }

                if (cd > 0) {
                    cd--;
                    if ((cd % Reflex.getInstance().getReflexConfig().getAutobanRemindInterval() == 0 || cd < 5) && cd != 0) {
                        FancyMessage fm = new FancyMessage(RLang.format(ReflexLang.ALERT_PREFIX));
                        fm.then(RLang.format(ReflexLang.AUTOBAN, player.getName(), check.getName(), cd + ""))
                                .tooltip(ChatColor.YELLOW + "Click here to cancel ban on " + player.getName())
                                .command("/reflex cancel " + player.getName());
                        AlertManager.staffMsg(fm);
                    }
                } else {
                    ban();
                    cancel();
                }
            }


        }.runTaskTimer(Reflex.getInstance(), 20L, 20L);
    }

    /**
     * Force the ban,
     * called by the #run method once the countdown is completed.
     * Unfreezes the player, removes their autoban, resets their VL, and bans using the configured AutobanMethod
     * Also calls a ReflexBanEvent that can be cancelled - ReflexBanEvent is ONLY called when the AutobanMethod is set to REFLEX
     */
    public void ban() {
        executed = true;
        if (player.getBukkitPlayer() != null && player.getBukkitPlayer().isOnline()) {
            Freeze.removeFreeze(player.getBukkitPlayer());
        }
        Reflex.getInstance().getAutobanManager().removeAutoban(player.getName());

        if (Reflex.getInstance().getReflexConfig().getAutobanMethod() == AutobanMethod.CONSOLE) {
            //Dispatch console command

            String format = Reflex.getInstance().getReflexConfig().getAutobanConsoleCommand();
            format = format.replace("{0}", player.getName());
            format = format.replace("{1}", check.getName());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), format);
        } else if (Reflex.getInstance().getReflexConfig().getAutobanMethod() == AutobanMethod.REFLEX) {
            //Reflex ban internally

            int seconds = Reflex.getInstance().getReflexConfig().getAutobanTimeMinutes() * 60;
            long mills = seconds * 1000;

            long expiry = System.currentTimeMillis() + mills;

            ReflexBan reflexBan = new ReflexBan(player.getUniqueId(), infraction, expiry);

            ReflexBanEvent event = new ReflexBanEvent(reflexBan);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                Reflex.getInstance().getBanManager().saveBan(reflexBan);


                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(reflexBan.getExpiration());

                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                boolean am = cal.get(Calendar.AM_PM) == Calendar.AM;

                String expires = "" + month + "/" + day + " @ " + hour + ":" + minute + " " + (am ? "AM" : "PM") + "&7 (" + cal.getTimeZone().getDisplayName() + ")";

                if (player != null) {
                    if (player.getBukkitPlayer() != null) {
                        if (player.getBukkitPlayer().isOnline()) {
                            player.getBukkitPlayer().kickPlayer(RLang.format(ReflexLang.BANNED, reflexBan.getCheckType().getName(), expires));
                        }
                    }
                }
            }
        } else {
            throw new ReflexRuntimeException("Unsupported ban method " + Reflex.getInstance().getReflexConfig().getAutobanMethod().toString());
        }

        FancyMessage fm = new FancyMessage(RLang.format(ReflexLang.ALERT_PREFIX));
        fm.then(RLang.format(ReflexLang.AUTOBAN_BANNED, player.getName(), check.getName()))
                .tooltip(ChatColor.YELLOW + "Click here to unban " + player.getName())
                .command("/reflex unban " + player.getName());
        AlertManager.staffMsg(fm);
    }

    /**
     * Cancel the autoban
     *
     * @param cancelled whether or not to cancel it
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        if (cancelled) {
            Reflex.getInstance().getAutobanManager().removeAutoban(player.getName());
            player.getRecord().resetViolations(check);
            if (player.getBukkitPlayer() != null && player.getBukkitPlayer().isOnline()) {
                if (Reflex.getInstance().getCheckManager().getCheck(check).isAutobanFreeze()) {
                    Player p = player.getBukkitPlayer();
                    Freeze.removeFreeze(p);
                    p.setAllowFlight(false);
                    player.msg(ReflexLang.AUTOBAN_CHEATER_CANCEL);
                }
            }
        }
    }

    public boolean isExecuted() {
        return executed;
    }

}
