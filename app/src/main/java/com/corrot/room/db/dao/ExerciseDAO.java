package com.corrot.room.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.corrot.room.db.entity.Exercise;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Insert
    void insertSingleExercise(Exercise exercise);

    @Insert
    void insertMultipleExercises(List<Exercise> exercises);

    @Query("SELECT * FROM Exercise WHERE workoutId=:workoutId")
    List<Exercise> getAllExercisesByWorkoutId(String workoutId);

    @Query("SELECT * FROM Exercise")
    LiveData<List<Exercise>> getAllExercises();    // For future diagrams :)

    @Query("SELECT * FROM Exercise WHERE Name=:name")
    List<Exercise> getAllExercises(String name);

    @Query("DELETE FROM Exercise WHERE workoutId=:workoutId")
    void deleteAllExercisesByWorkoutId(String workoutId);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);
}
