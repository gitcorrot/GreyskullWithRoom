package com.corrot.room.db.entity;

import com.corrot.room.db.converters.ExercisesConverter;

import java.io.Serializable;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "Routine")
@TypeConverters({ExercisesConverter.class})
public class Routine implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Label")
    public String label;

    @ColumnInfo(name = "Exercises")
    public List<String> exercises;

    @Ignore
    public boolean expanded;

    public Routine(String label, List<String> exercises) {
        this.label = label;
        this.exercises = exercises;
        this.expanded = false;
    }
}
