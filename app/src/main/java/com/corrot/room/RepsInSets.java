package com.corrot.room;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class RepsInSets {
    @TypeConverter
    public List<Integer> fromString(String s) {
        List<Integer> list = new ArrayList<>();
        String[] strings = s.split(", ");

        for(String str : strings) {
            list.add(Integer.parseInt(str));
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
