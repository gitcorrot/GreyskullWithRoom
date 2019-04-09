package com.corrot.room.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.corrot.room.db.dao.RoutineDAO;
import com.corrot.room.db.dao.ExerciseDAO;
import com.corrot.room.db.dao.WorkoutDAO;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Workout;

@Database(entities =  {Workout.class, Exercise.class, Routine.class},
          version = 1,
          exportSchema = false)
public abstract class WorkoutsDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "db_workouts";

    public abstract WorkoutDAO workoutDAO();
    public abstract ExerciseDAO exerciseDAO();
    public abstract RoutineDAO routineDAO();

    private static WorkoutsDatabase INSTANCE;

    public static WorkoutsDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (WorkoutsDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        WorkoutsDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
