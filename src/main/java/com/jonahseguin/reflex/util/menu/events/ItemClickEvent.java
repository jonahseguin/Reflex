/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */
package com.jonahseguin.reflex.util.menu.events;

import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashSet;
import java.util.Set;

/**
 * An event called when an Item in the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} is clicked.
 */
public class ItemClickEvent {
    private final ClickType clickType;
    private Player player;
    private boolean goBack = false;
    private boolean close = false;
    private boolean update = false;
    private boolean cancel = true;
    private Set<ClickType> acceptedClickTypes = new HashSet<>();

    public ItemClickEvent(Player player, ClickType clickType) {
        this.player = player;
        this.clickType = clickType;
        setAcceptedClickTypes(ClickType.LEFT);
    }

    /**
     * Gets the player who clicked.
     *
     * @return The player who clicked.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Checks if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will go back to the parent menu.
     *
     * @return True if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will go back to the parent menu, else false.
     */
    public boolean willGoBack() {
        return goBack;
    }

    /**
     * Sets if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will go back to the parent menu.
     *
     * @param goBack If the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will go back to the parent menu.
     */
    public void setWillGoBack(boolean goBack) {
        this.goBack = goBack;
        if (goBack) {
            close = false;
            update = false;
        }
    }

    /**
     * Checks if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will close.
     *
     * @return True if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will close, else false.
     */
    public boolean willClose() {
        return close;
    }

    /**
     * Sets if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will close.
     *
     * @param close If the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will close.
     */
    public void setWillClose(boolean close) {
        this.close = close;
        if (close) {
            goBack = false;
            update = false;
        }
    }

    /**
     * Checks if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will update.
     *
     * @return True if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will update, else false.
     */
    public boolean willUpdate() {
        return update;
    }

    /**
     * Sets if the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will update.
     *
     * @param update If the {@link com.jonahseguin.reflex.util.menu.menus.ItemMenu} will update.
     */
    public void setWillUpdate(boolean update) {
        this.update = update;
        if (update) {
            goBack = false;
            close = false;
        }
    }

    public ClickType getClickType() {
        return clickType;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public Set<ClickType> getAcceptedClickTypes() {
        return acceptedClickTypes;
    }

    public void setAcceptedClickTypes(ClickType... clickTypes) {
        this.acceptedClickTypes = Sets.newHashSet(clickTypes);
    }
}
