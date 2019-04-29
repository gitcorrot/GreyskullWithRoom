package com.corrot.room;

import android.util.Log;

import com.corrot.room.utils.MyTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BodyWeightItem implements Comparable<BodyWeightItem> {

    public float weight;
    public Date date;

    public BodyWeightItem(float weight, Date date) {
        this.weight = weight;
        this.date = date;
    }

    public BodyWeightItem(String itemString) {
        BodyWeightItem bodyWeightItem = fromString(itemString);
        this.weight = bodyWeightItem.weight;
        this.date = bodyWeightItem.date;
    }

    public String itemToString() {
        return this.weight + "_" + MyTimeUtils.parseDate(this.date, MyTimeUtils.MAIN_FORMAT);
    }

    private BodyWeightItem fromString(String itemString) {
        float weight;
        Date date;

        try {
            weight = Float.parseFloat(itemString.split("_")[0]);
        } catch (Exception e) {
            weight = 0f;
            Log.e("BodyWeightItem", e.getLocalizedMessage());
        }

        String dateString = itemString.split("_")[1];
        Locale locale = Locale.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat(MyTimeUtils.MAIN_FORMAT, locale);
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            Log.e("BodyWeightItem", e.getLocalizedMessage());
            date = new Date();
        }
        return new BodyWeightItem(weight, date);
    }

    @Override
    public int compareTo(BodyWeightItem o) {
        if (this.date == null || o.date == null) {
            return 0;
        }
        return o.date.compareTo(this.date);
    }
}
