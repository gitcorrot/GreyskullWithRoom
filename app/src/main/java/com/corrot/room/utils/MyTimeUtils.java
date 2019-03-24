package com.corrot.room.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyTimeUtils {

    public static String getCurrentDateString(String format) { // for example: "dd/MM/yyyy"
        final Date date = Calendar.getInstance().getTime();
        Locale locale = Locale.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }

    public static String parseDate(Date date, String format) {
        Locale locale = Locale.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }
}
