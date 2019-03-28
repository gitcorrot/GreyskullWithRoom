package com.corrot.room.utils;

import com.corrot.room.db.converters.DateConverter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Date;

public class ChartUtils {

    public static class DateAxisValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            Date date = DateConverter.toDate((long) value);
            return MyTimeUtils.parseDate(date, MyTimeUtils.MAIN_FORMAT);
        }
    }
}
