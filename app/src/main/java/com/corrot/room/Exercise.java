package com.corrot.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Workout.class,
                                  parentColumns = "id",
                                  childColumns = "exerciseId",
                                  onDelete = CASCADE))
@TypeConverters({RepsInSets.class})
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "exerciseId", index = true) // not sure if it's OK.
    public final int exerciseId;

    @ColumnInfo(name = "Name")
    public final String name;

    // TODO: change it for List<Float> weights or make ExerciseSet.class (2 variables)
    // TODO: not sure if class will be worse because of SQLite calls for charts.
    @ColumnInfo(name = "Weight")
    public final float weight;

    @ColumnInfo(name = "Reps in sets")
    public final List<Integer> repsInSets;

    public Exercise(final int exerciseId, String name,
                    float weight, List<Integer> repsInSets){
        this.exerciseId = exerciseId;
        this.name = name;
        this.weight = weight;
        this.repsInSets = repsInSets;
    }
}
