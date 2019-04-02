package com.corrot.room.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.corrot.room.db.converters.RepsConverter;
import com.corrot.room.db.converters.WeightsConverter;

import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Workout.class,
                                  parentColumns = "id",
                                  childColumns = "workoutId",
                                  onDelete = CASCADE))
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "workoutId", index = true) // not sure if it's OK.
    public String workoutId;

    @NonNull
    @ColumnInfo(name = "Name")
    public String name;

    @TypeConverters(WeightsConverter.class)
    @ColumnInfo(name = "Weights")
    public List<Float> weights;

    @TypeConverters(RepsConverter.class)
    @ColumnInfo(name = "Reps")
    public List<Integer> reps;

    public Exercise(@NonNull String workoutId, @NonNull String name, List<Float> weights, List<Integer> reps){
        this.workoutId = workoutId;
        this.name = name;
        this.weights = weights;
        this.reps = reps;
    }

    @Ignore
    public Exercise(int id, @NonNull String workoutId, @NonNull String name, List<Float> weights, List<Integer> reps){
        this.id = id;
        this.workoutId = workoutId;
        this.name = name;
        this.weights = weights;
        this.reps = reps;
    }
}
