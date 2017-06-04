/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.logger;

import com.jonahseguin.reflex.Reflex;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by Jonah Seguin on Sun 2017-05-28 at 19:17.
 * Project: Reflex
 */
public class RSpecialLogger {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd");
    private final Reflex reflex;
    private final File directory;
    private final Logger logger = Logger.getLogger(RSpecialLogger.class
            .getName());
    private String currentDate = "";
    private String name;
    private FileHandler fh = null;

    public RSpecialLogger(Reflex reflex, String name) {
        this.reflex = reflex;
        this.name = name;
        this.directory = new File(reflex.getDataFolder().getPath() + File.separator + "special_logs");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        updateCurrentDate();
        updateFormatter();

        logger.addHandler(fh);
    }

    public void log(String category, String msg) {
        log("[" + category.toUpperCase() + "] " + msg);
    }

    public void log(String msg) {
        preLog();
        logger.info(msg);
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
                    + currentDate + "_" + name + ".log");
        } catch (Exception e) {
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
                        + record.getMessage() + "\n";
            }
        });
    }

}
