package com.corrot.room.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.db.dao.DefinedWorkoutDAO;
import com.corrot.room.db.entity.DefinedWorkout;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DefinedWorkoutsRepository {

    private DefinedWorkoutDAO mDefinedWorkoutDAO;
    private LiveData<List<DefinedWorkout>> mAllWorkouts;

    public DefinedWorkoutsRepository(Application application) {
        WorkoutsDatabase db = WorkoutsDatabase.getInstance(application);
        mDefinedWorkoutDAO = db.definedWorkoutDAO();
        mAllWorkouts = mDefinedWorkoutDAO.getAllDefinedWorkouts();
    }

    public LiveData<List<DefinedWorkout>> getAllWorkouts() {
        return mAllWorkouts;
    }

    public void deleteAll() {
        new deleteAllAsync(mDefinedWorkoutDAO).execute();
    }

    public void insertSingleWorkout(DefinedWorkout workout) {
        new insertSingleWorkoutAsync(mDefinedWorkoutDAO).execute(workout);
    }

    public void updateWorkout(DefinedWorkout workout) {
        new updateWorkoutAsync(mDefinedWorkoutDAO).execute(workout);
    }

    public void deleteWorkout(DefinedWorkout workout) {
        new deleteWorkoutAsync(mDefinedWorkoutDAO).execute(workout);
    }

    /**********************************************************************************************
     *                                     ASYNC FUNCTIONS
     **********************************************************************************************/

    private static class deleteAllAsync extends AsyncTask<Void, Void, Void> {

        private final DefinedWorkoutDAO workoutDAO;

        deleteAllAsync(DefinedWorkoutDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workoutDAO.deleteAll();
            return null;
        }
    }

    private static class insertSingleWorkoutAsync extends AsyncTask<DefinedWorkout, Void, Void> {

        private final DefinedWorkoutDAO workoutDAO;

        insertSingleWorkoutAsync(DefinedWorkoutDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(final DefinedWorkout... params) {
            workoutDAO.insertSingleDefinedWorkout(params[0]);
            Log.d("DefinedWorkoutsRepo", "Defined workout inserted!");
            return null;
        }
    }

    private static class updateWorkoutAsync extends AsyncTask<DefinedWorkout, Void, Void> {

        private final DefinedWorkoutDAO workoutDAO;

        updateWorkoutAsync(DefinedWorkoutDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(final DefinedWorkout... params) {
            workoutDAO.updateWorkout(params[0]);
            return null;
        }
    }

    private static class deleteWorkoutAsync extends AsyncTask<DefinedWorkout, Void, Void> {

        private final DefinedWorkoutDAO workoutDAO;

        deleteWorkoutAsync(DefinedWorkoutDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(final DefinedWorkout... params) {
            workoutDAO.deleteWorkout(params[0]);
            return null;
        }
    }
}
