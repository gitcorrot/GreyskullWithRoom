package com.corrot.room.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;


import com.corrot.room.db.converters.DateConverter;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "Workout")
@TypeConverters({DateConverter.class})
public class Workout {

    @NonNull
    @PrimaryKey//(autoGenerate = true)
    public String id;

    @NonNull
    @ColumnInfo(name = "Date")
    public Date workoutDate;

    @Ignore
    public Workout(Date workoutDate) {
        this(UUID.randomUUID().toString(), workoutDate);
    }

    public Workout(String id, Date workoutDate) {
        this.id = id;
        this.workoutDate = workoutDate;
    }
}
