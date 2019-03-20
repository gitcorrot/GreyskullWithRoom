package com.corrot.room.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.corrot.room.db.converters.DateConverter;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "Workout")
@TypeConverters({DateConverter.class})
public class Workout {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "Date")
    public Date workoutDate;

    @ColumnInfo(name = "Type")
    public char type;

    public Workout(char type, Date workoutDate) {
        this.workoutDate = workoutDate;
        this.type = type;
    }
}
