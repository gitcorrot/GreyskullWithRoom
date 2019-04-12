package com.corrot.room.db;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.corrot.room.db.dao.ExerciseDAO;
import com.corrot.room.db.dao.RoutineDAO;
import com.corrot.room.db.dao.WorkoutDAO;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.db.entity.Workout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import androidx.core.content.ContextCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Workout.class, Exercise.class, Routine.class},
        version = 1,
        exportSchema = false)
public abstract class WorkoutsDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "db_workouts";

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

    public static boolean backup(Context context, String backupName) {

        boolean success = false;
        final String path = context.getDatabasePath(DATABASE_NAME).getPath();
        // TODO: backup name cant have spaces and stuff
        final String outPath = Environment.getExternalStorageDirectory() + "/" + backupName + ".db";

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                // TODO: Handle exceptions (a lot of exceptions)
                File dbFile = new File(path);
                FileInputStream fis = new FileInputStream(dbFile);
                OutputStream output = new FileOutputStream(outPath);

                Toast.makeText(context, outPath, Toast.LENGTH_SHORT).show();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                fis.close();
                success = true;
                Toast.makeText(context,
                        "Backup complete! Your file path:\n" + outPath, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e("Backup", e.getLocalizedMessage());
            }
        } else {
            Toast.makeText(context,
                    "Need permissions 'WRITE_EXTERNAL_STORAGE'!", Toast.LENGTH_SHORT).show();
        }

        return success;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
