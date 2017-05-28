/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */
package com.jonahseguin.reflex.util.menu;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.util.menu.menus.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Passes inventory click events to their menus for handling.
 */
public class MenuListener implements Listener {
    private static final MenuListener INSTANCE = new MenuListener();
    private Plugin plugin = null;

    private MenuListener() {
    }

    /**
     * Gets the {@link com.jonahseguin.reflex.util.menu.MenuListener} instance.
     *
     * @return The {@link com.jonahseguin.reflex.util.menu.MenuListener} instance.
     */
    public static MenuListener getInstance() {
        return INSTANCE;
    }

    /**
     * Closes all {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}s currently open.
     */
    public static void closeOpenMenus() {
        for (Player player : Reflex.getOnlinePlayers()) {
            if (player.getOpenInventory() != null) {
                Inventory inventory = player.getOpenInventory().getTopInventory();
                if (inventory.getHolder() instanceof MenuHolder) {
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getHolder() instanceof MenuHolder) {
            event.setCancelled(true);
            ((MenuHolder) event.getInventory().getHolder()).getMenu().onInventoryClick(event);
        }
    }

    /**
     * Registers the events of the {@link com.jonahseguin.reflex.util.menu.MenuListener} to a plugin.
     *
     * @param plugin The plugin used to register the events.
     */
    public void register(JavaPlugin plugin) {
        if (!isRegistered(plugin)) {
            plugin.getServer().getPluginManager().registerEvents(INSTANCE, plugin);
            this.plugin = plugin;
        }
    }

    /**
     * Checks if the {@link com.jonahseguin.reflex.util.menu.MenuListener} is registered to a plugin.
     *
     * @param plugin The plugin.
     * @return True if the {@link com.jonahseguin.reflex.util.menu.MenuListener} is registered to the plugin, else false.
     */
    public boolean isRegistered(JavaPlugin plugin) {
        if (plugin.equals(this.plugin)) {
            for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
                if (listener.getListener().equals(INSTANCE)) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(plugin)) {
            closeOpenMenus();
            plugin = null;
        }
    }
}
