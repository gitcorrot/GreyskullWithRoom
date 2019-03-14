package com.corrot.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
interface ExerciseDAO {
    @Insert
    void insertSingleExercise(Exercise exercise);

    /*@Insert
    void insertMultipleExercises(List<Exercise> exercises);*/

    @Query("SELECT * FROM Exercise WHERE id=:id")
    LiveData<List<Exercise>> getAllExercisesByWorkoutId(long id);

    @Query("SELECT * FROM Exercise")
    LiveData<List<Exercise>> getAllExercises();    // For future diagrams :)

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);
}
