package com.corrot.room.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;


import com.corrot.room.db.converters.DateConverter;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "Workout")
@TypeConverters({DateConverter.class})
public class Workout {

    @NonNull
    @PrimaryKey//(autoGenerate = true)
    public String id;

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
