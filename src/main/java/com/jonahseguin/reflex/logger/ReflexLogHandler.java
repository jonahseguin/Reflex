/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.logger;

import com.jonahseguin.reflex.Reflex;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Jonah Seguin on Mon 2017-06-05 at 18:59.
 * Project: Reflex
 */
public class ReflexLogHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().equals(Level.WARNING) || record.getLevel().equals(Level.SEVERE)) {
            if (record.getThrown() != null) {
                Reflex.getReflexLogger().error(record.getThrown());
            } else {
                Reflex.getReflexLogger().error(record.getMessage());
            }
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
