package com.corrot.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Insert
    void insertSingleWorkout(Workout workout);

    @Query("SELECT * FROM Workout")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("DELETE FROM Workout")
    void deleteAll();

    @Update
    void updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);
}
