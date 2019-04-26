package com.corrot.room.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM Exercise WHERE Name=:name")
    LiveData<List<Exercise>> getAllExercisesWithName(String name);

    @Query("DELETE FROM Exercise WHERE workoutId=:workoutId")
    void deleteAllExercisesByWorkoutId(String workoutId);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);
}
