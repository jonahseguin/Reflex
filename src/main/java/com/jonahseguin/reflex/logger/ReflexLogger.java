/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.logger;

import com.jonahseguin.reflex.Reflex;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Jonah Seguin on Sun 2017-05-28 at 19:17.
 * Project: Reflex
 */
public class ReflexLogger {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd");
    private final Reflex reflex;
    private final File directory;
    private final Logger logger = Logger.getLogger(ReflexLogger.class
            .getName());
    private String currentDate = "";
    private FileHandler fh = null;

    public ReflexLogger(Reflex reflex) {
        this.reflex = reflex;
        this.directory = new File(reflex.getDataFolder().getPath() + File.separator + "logs");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        updateCurrentDate();
        updateFormatter();

        logger.addHandler(fh);
    }

    private void updateCurrentDate() {
        String newDate = dateFormat.format(Calendar.getInstance().getTime());
        if (!newDate.equals(currentDate)) {

            this.currentDate = newDate;
        }
    }

    private void updateFormatter() {
        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
        try {
            fh = new FileHandler(directory.getPath() + File.separator
                    + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter()); // todo
    }


    private String getDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return year + "_" + month + "_" + day;
    }

}
