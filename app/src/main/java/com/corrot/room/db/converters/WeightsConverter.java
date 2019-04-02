package com.corrot.room.db.converters;

import androidx.room.TypeConverter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WeightsConverter {
    @TypeConverter
    public List<Float> fromString(String s) {
        List<Float> list = new ArrayList<>();
        String[] strings = s.split(", ");

        for(String str : strings) {
            if(!str.isEmpty()) {
                try {
                    list.add(Float.parseFloat(str));
                } catch (NumberFormatException e) {
                    Log.e("WeightsConverter", e.getLocalizedMessage());
                }
            }
        }
        return list;
    }

    @TypeConverter
    public String toString(List<Float> weights) {
        String s = weights.toString();
        s = s.substring(1, s.length()-1);
        return s;
    }
}
