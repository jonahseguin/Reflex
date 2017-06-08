/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */
package com.jonahseguin.reflex.util.menu.menus;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.util.menu.MenuListener;
import com.jonahseguin.reflex.util.menu.events.ItemClickEvent;
import com.jonahseguin.reflex.util.menu.items.MenuItem;
import com.jonahseguin.reflex.util.menu.items.StaticMenuItem;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A Menu controlled by ItemStacks in an Inventory.
 */
public class ItemMenu {
    /**
     * The {@link StaticMenuItem} that appears in empty slots if {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu#fillEmptySlots()} is called.
     */
    @SuppressWarnings("deprecation")
    private static final MenuItem EMPTY_SLOT_ITEM = new StaticMenuItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData()));
    private Reflex plugin;
    private String name;
    private Size size;
    private MenuItem[] items;
    private ItemMenu parent;

    /**
     * Creates an {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     *
     * @param name   The name of the inventory.
     * @param size   The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu.Size} of the inventory.
     * @param plugin The {@link JavaPlugin} instance.
     * @param parent The ItemMenu's parent.
     */
    public ItemMenu(String name, Size size, Reflex plugin, ItemMenu parent) {
        this.plugin = plugin;
        this.name = name;
        this.size = size;
        this.items = new MenuItem[size.getSize()];
        this.parent = parent;
    }

    /**
     * Creates an {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} with no parent.
     *
     * @param name   The name of the inventory.
     * @param size   The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu.Size} of the inventory.
     * @param plugin The Plugin instance.
     */
    public ItemMenu(String name, Size size, Reflex plugin) {
        this(name, size, plugin, null);
    }

    /**
     * Gets the name of the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     *
     * @return The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}'s name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu.Size} of the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     *
     * @return The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}'s {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu.Size}.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Checks if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} has a parent.
     *
     * @return True if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} has a parent, else false.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Gets the parent of the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     *
     * @return The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}'s parent.
     */
    public ItemMenu getParent() {
        return parent;
    }

    /**
     * Sets the parent of the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     *
     * @param parent The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}'s parent.
     */
    public void setParent(ItemMenu parent) {
        this.parent = parent;
    }

    /**
     * Sets the {@link MenuItem} of a slot.
     *
     * @param position The slot position.
     * @param menuItem The {@link MenuItem}.
     * @return The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     */
    public ItemMenu setItem(int position, MenuItem menuItem) {
        items[position] = menuItem;
        return this;
    }

    /**
     * Fills all empty slots in the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} with a certain {@link MenuItem}.
     *
     * @param menuItem The {@link MenuItem}.
     * @return The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     */
    public ItemMenu fillEmptySlots(MenuItem menuItem) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = menuItem;
            }
        }
        return this;
    }

    /**
     * Fills all empty slots in the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} with the default empty slot item.
     *
     * @return The {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     */
    public ItemMenu fillEmptySlots() {
        return fillEmptySlots(EMPTY_SLOT_ITEM);
    }

    /**
     * Opens the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} for a player.
     *
     * @param player The player.
     */
    public void open(Player player) {
        if (!MenuListener.getInstance().isRegistered(plugin)) {
            MenuListener.getInstance().register(plugin);
        }
        Inventory inventory = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, size.getSize())), size.getSize(), name);
        apply(inventory, player);
        player.openInventory(inventory);
    }

    /**
     * Updates the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} for a player.
     *
     * @param player The player to update the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} for.
     */
    @SuppressWarnings("deprecation")
    public void update(Player player) {
        if (player.getOpenInventory() != null) {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (inventory.getHolder() instanceof MenuHolder && ((MenuHolder) inventory.getHolder()).getMenu().equals(this)) {
                apply(inventory, player);
                player.updateInventory();
            }
        }
    }

    /**
     * Applies the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} for a player to an Inventory.
     *
     * @param inventory The Inventory.
     * @param player    The Player.
     */
    private void apply(Inventory inventory, Player player) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                inventory.setItem(i, items[i].getFinalIcon(player));
            } else {
                inventory.setItem(i, null);
            }
        }
    }

    /**
     * Handles InventoryClickEvents for the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     */
    @SuppressWarnings("deprecation")
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot >= 0 && slot < size.getSize() && items[slot] != null) {
            Player player = (Player) event.getWhoClicked();
            ItemClickEvent itemClickEvent = new ItemClickEvent(player, event.getClick());
            items[slot].onItemClick(itemClickEvent);
            if (itemClickEvent.isCancel()) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
            if (!itemClickEvent.getAcceptedClickTypes().contains(event.getClick())) return;
            if (itemClickEvent.willUpdate()) {
                update(player);
            } else {
                player.updateInventory();
                if (itemClickEvent.willClose() || itemClickEvent.willGoBack()) {
                    final String playerName = player.getName();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Player p = Bukkit.getPlayerExact(playerName);
                        if (p != null) {
                            p.closeInventory();
                        }
                    }, 1);
                }
                if (itemClickEvent.willGoBack() && hasParent()) {
                    final String playerName = player.getName();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Player p = Bukkit.getPlayerExact(playerName);
                        if (p != null) {
                            parent.open(p);
                        }
                    }, 3);
                }
            }
        }
    }

    /**
     * Destroys the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     */
    public void destroy() {
        plugin = null;
        name = null;
        size = null;
        items = null;
        parent = null;
    }

    /**
     * Possible sizes of an {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu}.
     */
    public enum Size {
        ONE_LINE(9),
        TWO_LINE(18),
        THREE_LINE(27),
        FOUR_LINE(36),
        FIVE_LINE(45),
        SIX_LINE(54);

        private final int size;

        Size(int size) {
            this.size = size;
        }

        /**
         * Gets the required {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu.Size} for an amount of slots.
         *
         * @param slots The amount of slots.
         * @return The required {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu.Size}.
         */
        public static Size fit(int slots) {
            if (slots < 10) {
                return ONE_LINE;
            } else if (slots < 19) {
                return TWO_LINE;
            } else if (slots < 28) {
                return THREE_LINE;
            } else if (slots < 37) {
                return FOUR_LINE;
            } else if (slots < 46) {
                return FIVE_LINE;
            } else {
                return SIX_LINE;
            }
        }

        /**
         * Gets the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu.Size}'s amount of slots.
         *
         * @return The amount of slots.
         */
        public int getSize() {
            return size;
        }
    }
}
