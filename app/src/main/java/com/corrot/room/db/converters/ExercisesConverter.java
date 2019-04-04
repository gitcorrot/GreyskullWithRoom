package com.corrot.room.db.converters;

import java.util.Arrays;
import java.util.List;

import androidx.room.TypeConverter;

public class ExercisesConverter {
    @TypeConverter
    public List<String> fromString(String s) {
        return Arrays.asList(s.split(", "));
    }

    @TypeConverter
    public String toString(List<String> exercisesList) {
        String s = exercisesList.toString();
        s = s.substring(1, s.length() - 1);
        return s;
    }
}
