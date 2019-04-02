package com.corrot.room.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
