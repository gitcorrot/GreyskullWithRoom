package com.corrot.room.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.corrot.room.db.converters.RepsInSets;
import com.corrot.room.db.converters.WeightsConverter;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Workout.class,
                                  parentColumns = "id",
                                  childColumns = "workoutId",
                                  onDelete = CASCADE))
public class Exercise {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "workoutId", index = true) // not sure if it's OK.
    public String workoutId;

    @ColumnInfo(name = "Name")
    public String name;

    @TypeConverters(WeightsConverter.class)
    @ColumnInfo(name = "Weights")
    public List<Float> weights;

    @TypeConverters(RepsInSets.class)
    @ColumnInfo(name = "Reps")
    public List<Integer> reps;

    public Exercise(String workoutId, String name, List<Float> weights, List<Integer> reps){
        //this.id = id;
        this.workoutId = workoutId;
        this.name = name;
        this.weights = weights;
        this.reps = reps;
    }
}
