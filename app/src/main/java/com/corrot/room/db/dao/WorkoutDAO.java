package com.corrot.room.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.corrot.room.db.entity.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Insert
    void insertSingleWorkout(Workout workout);

    @Query("SELECT * FROM Workout")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM Workout WHERE id=:id")
    Workout getWorkoutById(String id);

    @Query("DELETE FROM Workout")
    void deleteAll();

    //SELECT * FROM Workout ORDER BY Date <-- returns

    //SELECT datetime(1553426734, 'unixepoch', 'localtime')

    //SELECT * FROM Workout WHERE Date > strftime('%s','2004-01-01 02:34:56')

    @Update
    void updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);
}
