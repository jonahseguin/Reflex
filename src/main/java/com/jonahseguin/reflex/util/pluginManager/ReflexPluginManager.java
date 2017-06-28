/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.pluginManager;

import com.google.common.collect.Lists;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.event.*;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;

public abstract class ReflexPluginManager implements PluginManager {

    private PluginManager delegate;

    public ReflexPluginManager(Plugin owner) {
        this(owner.getServer().getPluginManager());
    }

    public ReflexPluginManager(PluginManager delegate) {
        this.delegate = delegate;
    }

    /**
     * Invoked when an error occurs in a event listener.
     *
     * @param event - the event where the error occured.
     * @param e     - error that occured.
     */
    protected abstract void customHandler(Event event, Throwable e);

    /**
     * Registers all the events in the given listener class.
     *
     * @param listener - listener to register
     * @param plugin   - plugin to register
     */
    public void registerEvents(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled())
            throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");

        // Just in case Bukkit decides to validate the parameters in the future
        EventExecutor nullExecutor = new EventExecutor() {
            @Override
            public void execute(Listener arg0, Event arg1) throws EventException {
                throw new IllegalStateException("This method should never be called!");
            }
        };

        for (Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin
                .getPluginLoader().createRegisteredListeners(listener, plugin)
                .entrySet()) {

            Collection<RegisteredListener> listeners = entry.getValue();
            Collection<RegisteredListener> modified = Lists.newArrayList();

            // Use our plugin specific logger instead
            for (Iterator<RegisteredListener> it = listeners.iterator(); it.hasNext(); ) {
                final RegisteredListener delegate = it.next();

                RegisteredListener customListener = new RegisteredListener(
                        delegate.getListener(), nullExecutor, delegate.getPriority(),
                        delegate.getPlugin(), delegate.isIgnoringCancelled()) {
                    @Override
                    public void callEvent(Event event) throws EventException {
                        try {
                            delegate.callEvent(event);
                        } catch (AuthorNagException e) {
                            // Let Bukkit handle that one
                            throw e;
                        } catch (Throwable e) {
                            customHandler(event, e);
                        }
                    }
                };

                modified.add(customListener);
            }

            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(modified);
        }
    }

    private EventExecutor getWrappedExecutor(final EventExecutor executor) {
        return new EventExecutor() {
            @Override
            public void execute(Listener listener, Event event) throws EventException {
                // Just like above
                try {
                    executor.execute(listener, event);
                } catch (AuthorNagException e) {
                    throw e;
                } catch (Throwable e) {
                    customHandler(event, e);
                }
            }
        };
    }

    private HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;

        } catch (NoSuchMethodException e) {
            if ((clazz.getSuperclass() != null)
                    && (!clazz.getSuperclass().equals(Event.class))
                    && (Event.class.isAssignableFrom(clazz.getSuperclass()))) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(
                        Event.class));
            }
        }
        throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
    }

    @Override
    public void registerEvent(
            Class<? extends Event> event, Listener listener, EventPriority priority,
            EventExecutor executor, Plugin plugin) {

        delegate.registerEvent(event, listener, priority, getWrappedExecutor(executor), plugin);
    }

    @Override
    public void registerEvent(
            Class<? extends Event> event, Listener listener,
            EventPriority priority, EventExecutor executor, Plugin plugin, boolean ignoreCancel) {

        delegate.registerEvent(event, listener, priority, getWrappedExecutor(executor), plugin);
    }

    @Override
    public void registerInterface(Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        delegate.registerInterface(loader);
    }

    @Override
    public void addPermission(Permission perm) {
        delegate.addPermission(perm);
    }

    @Override
    public void callEvent(Event event) throws IllegalStateException {
        delegate.callEvent(event);
    }

    @Override
    public void clearPlugins() {
        delegate.clearPlugins();
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        delegate.disablePlugin(plugin);
    }

    @Override
    public void disablePlugins() {
        delegate.disablePlugins();
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        delegate.enablePlugin(plugin);
    }

    @Override
    public Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        return delegate.getDefaultPermSubscriptions(op);
    }

    @Override
    public Set<Permission> getDefaultPermissions(boolean op) {
        return delegate.getDefaultPermissions(op);
    }

    @Override
    public Permission getPermission(String name) {
        return delegate.getPermission(name);
    }

    @Override
    public Set<Permissible> getPermissionSubscriptions(String permission) {
        return delegate.getPermissionSubscriptions(permission);
    }

    @Override
    public Set<Permission> getPermissions() {
        return delegate.getPermissions();
    }

    @Override
    public Plugin getPlugin(String name) {
        return delegate.getPlugin(name);
    }

    @Override
    public Plugin[] getPlugins() {
        return delegate.getPlugins();
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return delegate.isPluginEnabled(name);
    }

    @Override
    public boolean isPluginEnabled(Plugin plugin) {
        return delegate.isPluginEnabled(plugin);
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        return delegate.loadPlugin(file);
    }

    @Override
    public Plugin[] loadPlugins(File directory) {
        return delegate.loadPlugins(directory);
    }

    @Override
    public void recalculatePermissionDefaults(Permission permission) {
        delegate.recalculatePermissionDefaults(permission);
    }

    @Override
    public void removePermission(Permission perm) {
        delegate.removePermission(perm);
    }

    @Override
    public void removePermission(String name) {
        delegate.removePermission(name);
    }

    @Override
    public void subscribeToDefaultPerms(boolean op, Permissible permissable) {
        delegate.subscribeToDefaultPerms(op, permissable);
    }

    @Override
    public void subscribeToPermission(String permission, Permissible permissable) {
        delegate.subscribeToPermission(permission, permissable);
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op, Permissible permissable) {
        delegate.unsubscribeFromDefaultPerms(op, permissable);
    }

    @Override
    public void unsubscribeFromPermission(String permission, Permissible permissable) {
        delegate.unsubscribeFromPermission(permission, permissable);
    }

    @Override
    public boolean useTimings() {
        return delegate.useTimings();
    }
}