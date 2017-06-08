/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.obj;

import java.util.Calendar;

public class TimeUtil {

    public static String format(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        int d = cal.get(Calendar.DAY_OF_MONTH);
        int mo = cal.get(Calendar.MONTH) + 1;
        int h = cal.get(Calendar.HOUR);
        int m = cal.get(Calendar.MINUTE);

        String day = "" + d;
        String month = "" + mo;
        String hour = "" + h;
        String minute = (m < 10 ? "0" : "") + "" + m;
        boolean am = cal.get(Calendar.AM_PM) == Calendar.AM;

        return "" + month + "/" + day + " - " + hour + ":" + minute + " " + (am ? "AM" : "PM") + " (" + cal.getTimeZone().getDisplayName() + ")";
    }

    public static String shortDateFormat(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        int d = cal.get(Calendar.DAY_OF_MONTH);
        int mo = cal.get(Calendar.MONTH) + 1;
        int ye = cal.get(Calendar.YEAR);

        String day = "" + d;
        String month = "" + mo;
        String year = "" + ye;

        return year + "/" + month + "/" + day; // 2017/05/20
    }

}
