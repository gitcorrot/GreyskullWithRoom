package com.corrot.room.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.corrot.room.db.converters.RepsInSets;
import com.corrot.room.db.converters.WeightsConverter;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Workout.class,
                                  parentColumns = "id",
                                  childColumns = "exerciseId",
                                  onDelete = CASCADE))
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "exerciseId", index = true) // not sure if it's OK.
    public final int exerciseId;

    @ColumnInfo(name = "Name")
    public final String name;

    @TypeConverters(WeightsConverter.class)
    @ColumnInfo(name = "Weights")
    public final List<Float> weights;

    @TypeConverters(RepsInSets.class)
    @ColumnInfo(name = "Reps")
    public final List<Integer> reps;

    public Exercise(final int exerciseId, String name,
                    List<Float> weights, List<Integer> reps){
        this.exerciseId = exerciseId;
        this.name = name;
        this.weights = weights;
        this.reps = reps;
    }
}
