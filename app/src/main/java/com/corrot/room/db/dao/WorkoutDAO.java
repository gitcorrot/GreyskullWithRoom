package com.corrot.room.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.corrot.room.db.converters.DateConverter;
import com.corrot.room.db.entity.Workout;

import java.util.Date;
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

    @Query("SELECT COUNT(*) FROM Workout")
    LiveData<Integer> getTotalCount();

    @Query("SELECT * FROM Workout ORDER BY date DESC LIMIT 1;")
    LiveData<Workout> getLastWorkout();

    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM Workout WHERE strftime('%Y-%m-%d', Date / 1000, 'unixepoch', 'localtime') " +
            "= strftime('%Y-%m-%d', :date / 1000, 'unixepoch', 'localtime')")
    List<Workout> getWorkoutsByDate(Date date);

    @Update
    void updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);
}
