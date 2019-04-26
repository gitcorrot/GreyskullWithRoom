package com.corrot.room.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.corrot.room.db.converters.ExercisesConverter;
import com.corrot.room.db.converters.RepsConverter;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Routine")             // works OK for sets
@TypeConverters({ExercisesConverter.class, RepsConverter.class})
public class Routine implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Label")
    public String label;

    @ColumnInfo(name = "Exercises")
    public List<String> exercises;

    @ColumnInfo(name = "Sets")
    public List<Integer> sets;

    @Ignore
    public boolean expanded;

    public Routine(String label, List<String> exercises, List<Integer> sets) {
        this.label = label;
        this.exercises = exercises;
        this.sets = sets;
        this.expanded = false;
    }
}
