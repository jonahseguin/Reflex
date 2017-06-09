/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.logger;

import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.check.alert.AlertManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

import org.bukkit.ChatColor;

/**
 * Created by Jonah Seguin on Sun 2017-05-28 at 19:17.
 * Project: Reflex
 */
public class ReflexLogger {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    private final Reflex reflex;
    private final File directory;
    private final String fileName;
    private final Logger logger = Logger.getLogger(ReflexLogger.class
            .getName());
    private String currentDate = "";
    private FileHandler fh = null;

    public ReflexLogger(Reflex reflex, String fileName) {
        this.reflex = reflex;
        this.fileName = fileName;
        this.directory = new File(reflex.getDataFolder().getPath() + File.separator + "logs");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        reflex.getLogger().addHandler(new ReflexLogHandler()); // Catch errors
        updateCurrentDate();
        updateFormatter();
        logger.addHandler(fh);
    }

    public void log(String category, String msg) {
        log("[" + category.toUpperCase() + "] " + msg);
    }

    public void log(String msg) {
        logNoStaffMsg(msg);
    }

    public void info(String msg) {
        log(msg);
    }

    public void logNoStaffMsg(String msg) {
        preLog();
        logger.info(msg);
        //reflex.getLogger().info(msg);
    }

    public void error(String msg, Throwable throwable) {
        preLog();
        logger.log(Level.INFO, "[ERROR] " + msg + " (" + throwable.getMessage() + ")", throwable);
        //reflex.getLogger().info("[REFLEX ERROR] " + msg + " (" + throwable.getMessage() + ")");
        AlertManager.staffMsg(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GRAY + msg + " (" + throwable.getMessage() + ")");
    }

    public void error(Throwable throwable) {
        preLog();
        logger.log(Level.INFO, "[ERROR]" + throwable.getMessage(), throwable);
        //reflex.getLogger().info("[REFLEX ERROR] " + throwable.getMessage());
        AlertManager.staffMsg(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GRAY + throwable.getMessage());
    }

    public void error(String msg) {
        preLog();
        logger.warning("[ERROR]" + msg);
        AlertManager.staffMsg(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GRAY + msg);
    }

    private void preLog() {
        if (!updateCurrentDate()) {
            logger.removeHandler(fh);
            updateFormatter();
            logger.addHandler(fh);
        }
    }

    private boolean updateCurrentDate() {
        String newDate = dateFormat.format(Calendar.getInstance().getTime());
        if (!newDate.equals(currentDate)) {
            this.currentDate = newDate;
            return false;
        }
        return true;
    }

    private void updateFormatter() {
        try {
            fh = new FileHandler(directory.getPath() + File.separator
                    + currentDate + "-" + fileName + ".log");
        } catch (IOException | SecurityException e) {
            error("INTERNAL LOGGER ERROR", e);
            e.printStackTrace();
        }
        fh.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                // [05-27-2017 17:12:22]: Message
                SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(record.getMillis());
                return "[" + logTime.format(cal.getTime()) + "]: "
                        + record.getMessage() + LINE_SEPARATOR;
            }
        });
    }

}
