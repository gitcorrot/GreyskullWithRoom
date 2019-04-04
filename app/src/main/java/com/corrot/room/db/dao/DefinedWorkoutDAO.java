package com.corrot.room.db.dao;

import com.corrot.room.db.entity.DefinedWorkout;
import com.corrot.room.db.entity.Workout;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DefinedWorkoutDAO {
    @Insert
    void insertSingleDefinedWorkout(DefinedWorkout definedWorkout);

    @Query("SELECT * FROM `Defined workout`")
    LiveData<List<DefinedWorkout>> getAllDefinedWorkouts();

    @Query("DELETE FROM `Defined workout`")
    void deleteAll();

    @Update
    void updateWorkout(DefinedWorkout definedWorkout);

    @Delete
    void deleteWorkout(DefinedWorkout definedWorkout);
}
