/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.pluginManager;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Implements a delegating scheduler that automatically handles all exceptions.
 *
 * @author Kristian
 */
public abstract class ReflexScheduler implements BukkitScheduler {

    // A reference to the underlying scheduler
    private Plugin plugin;
    private BukkitScheduler delegate;

    public ReflexScheduler(Plugin owner) {
        this.plugin = owner;
        this.delegate = owner.getServer().getScheduler();
    }

    /**
     * Invoked when an error occurs in a task.
     *
     * @param taskID - unique ID of the task, or
     * @param e      - error that occured.
     */
    protected abstract void customHandler(int taskID, Throwable e);

    @Override
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        return delegate.callSyncMethod(plugin, task);
    }

    @Override
    public void cancelAllTasks() {
        delegate.cancelAllTasks();
    }

    @Override
    public void cancelTask(int taskId) {
        delegate.cancelTask(taskId);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        delegate.cancelTasks(plugin);
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        return delegate.getActiveWorkers();
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        return delegate.getPendingTasks();
    }

    @Override
    public boolean isCurrentlyRunning(int taskId) {
        return delegate.isCurrentlyRunning(taskId);
    }

    @Override
    public boolean isQueued(int taskId) {
        return delegate.isQueued(taskId);
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleAsyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncDelayedTask(plugin, wrapped, delay));
        return wrapped.getTaskID();
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        wrapped.setTaskID(delegate.scheduleSyncRepeatingTask(plugin, wrapped, delay, period));
        return wrapped.getTaskID();
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable task) throws IllegalArgumentException {
        TaskedRunnable wrapped = new TaskedRunnable(task);

        BukkitTask bukkitTask = delegate.runTaskAsynchronously(plugin, task);
        wrapped.setTaskID(bukkitTask.getTaskId());
        return bukkitTask;
    }

    public BukkitTask asyncTask(Runnable task) {
        return this.runTaskAsynchronously(plugin, task);
    }


    /*
     * Shortened Reflex scheduling methods
     */

    public int asyncRepeatingTask(Runnable task, long delay, long period) {
        return this.scheduleAsyncRepeatingTask(plugin, task, delay, period);
    }

    public int syncRepeatingTask(Runnable task, long delay, long period) {
        return this.scheduleSyncRepeatingTask(plugin, task, delay, period);
    }

    public int asyncDelayedTask(Runnable task, long delay) {
        return this.scheduleAsyncDelayedTask(plugin, task, delay);
    }

    public int syncDelayedTask(Runnable task, long delay) {
        return this.scheduleSyncDelayedTask(plugin, task, delay);
    }

    private class TaskedRunnable implements Runnable {

        private int taskID = -1;
        private Runnable delegate;

        public TaskedRunnable(Runnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                delegate.run();
            } catch (Throwable e) {
                customHandler(taskID, e);
            }
        }

        public int getTaskID() {
            return taskID;
        }

        public void setTaskID(int taskID) {
            this.taskID = taskID;
        }
    }


}