package com.corrot.room.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.db.dao.WorkoutDAO;
import com.corrot.room.db.entity.Workout;

import java.util.List;

public class WorkoutsRepository {

    private WorkoutDAO mWorkoutDAO;
    private LiveData<List<Workout>> mAllWorkouts;

    public WorkoutsRepository(Application application) {
        WorkoutsDatabase db = WorkoutsDatabase.getInstance(application);    // Not sure if it's ok
        mWorkoutDAO = db.workoutDAO();
        mAllWorkouts = mWorkoutDAO.getAllWorkouts();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return mAllWorkouts;
    }

    public void deleteAll() {
        new deleteAllAsync(mWorkoutDAO).execute();
    }

    public void insertSingleWorkout(Workout workout) {
        new insertSingleWorkoutAsync(mWorkoutDAO).execute(workout);
    }

    public void updateWorkout(Workout workout) {
        new updateWorkoutAsync(mWorkoutDAO).execute(workout);
    }

    public void deleteWorkout(Workout workout) {
        new deleteWorkoutAsync(mWorkoutDAO).execute(workout);
    }

     /**********************************************************************************************
     *                                     ASYNC FUNCTIONS
     **********************************************************************************************/

    private static class deleteAllAsync extends AsyncTask<Void, Void, Void> {

        private final WorkoutDAO workoutDAO;

        deleteAllAsync(WorkoutDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workoutDAO.deleteAll();
            return null;
        }
    }

    private static class insertSingleWorkoutAsync extends AsyncTask<Workout, Void, Void> {

        private final WorkoutDAO workoutDAO;

        insertSingleWorkoutAsync(WorkoutDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(final Workout... params) {
            workoutDAO.insertSingleWorkout(params[0]);
            Log.d("asdasd", "Workout inserted!");
            return null;
        }
    }

    private static class updateWorkoutAsync extends AsyncTask<Workout, Void, Void> {

        private final WorkoutDAO workoutDAO;

        updateWorkoutAsync(WorkoutDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(final Workout... params) {
            workoutDAO.updateWorkout(params[0]);
            return null;
        }
    }

    private static class deleteWorkoutAsync extends AsyncTask<Workout, Void, Void> {

        private final WorkoutDAO workoutDAO;

        deleteWorkoutAsync(WorkoutDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(final Workout... params) {
            workoutDAO.deleteWorkout(params[0]);
            return null;
        }
    }
}
