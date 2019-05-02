package com.corrot.room.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.corrot.room.db.converters.DateConverter;
import com.corrot.room.db.converters.RepsConverter;
import com.corrot.room.db.converters.WeightsConverter;

import java.util.Date;
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
    @ColumnInfo(name = "workoutId")
    public String workoutId;

    @TypeConverters(DateConverter.class)
    @NonNull
    @ColumnInfo(name = "workoutDate")
    public Date workoutDate;

    @ColumnInfo(name = "workoutLabel")
    public String workoutLabel;

    @NonNull
    @ColumnInfo(name = "Name")
    public String name;

    @TypeConverters(WeightsConverter.class)
    @ColumnInfo(name = "Weights")
    public List<Float> weights;

    @TypeConverters(RepsConverter.class)
    @ColumnInfo(name = "Reps")
    public List<Integer> reps;

    public Exercise(@NonNull String workoutId, @NonNull Date workoutDate, String workoutLabel,
                    @NonNull String name, List<Float> weights, List<Integer> reps) {
        this.workoutId = workoutId;
        this.workoutDate = workoutDate;
        this.workoutLabel = workoutLabel;
        this.name = name;
        this.weights = weights;
        this.reps = reps;
    }

    @Ignore
    public Exercise(int id, @NonNull String workoutId, @NonNull Date workoutDate, String workoutLabel,
                    @NonNull String name, List<Float> weights, List<Integer> reps) {
        this.id = id;
        this.workoutId = workoutId;
        this.workoutDate = workoutDate;
        this.workoutLabel = workoutLabel;
        this.name = name;
        this.weights = weights;
        this.reps = reps;
    }
}
