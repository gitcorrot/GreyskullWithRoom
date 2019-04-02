package com.corrot.room.db.converters;

import androidx.room.TypeConverter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RepsConverter {
    @TypeConverter
    public List<Integer> fromString(String s) {
        List<Integer> list = new ArrayList<>();
        String[] strings = s.split(", ");

        for(String str : strings) {
            if(!str.isEmpty()) {
                try {
                    list.add(Integer.parseInt(str));
                } catch (NumberFormatException e) {
                    Log.e("RepsConverter", e.getLocalizedMessage());
                }
            }
        }
        return list;
    }

    @TypeConverter
    public String toString(List<Integer> repsInSets) {
        String s = repsInSets.toString();
        s = s.substring(1, s.length()-1);
        return s;
    }
}
