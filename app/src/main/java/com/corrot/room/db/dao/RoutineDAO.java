package com.corrot.room.db.dao;

import com.corrot.room.db.entity.Routine;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RoutineDAO {
    @Insert
    void insertSingleRoutine(Routine routine);

    @Query("SELECT * FROM 'Routine'")
    LiveData<List<Routine>> getAllRoutines();

    @Query("SELECT * FROM 'Routine' WHERE Label=:label")
    Routine getRoutineByName(String label);

    @Query("DELETE FROM Routine")
    void deleteAll();

    @Update
    void updateRoutine(Routine routine);

    @Delete
    void deleteRoutine(Routine routine);
}
