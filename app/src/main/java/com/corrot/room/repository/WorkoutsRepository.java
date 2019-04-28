package com.corrot.room.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.corrot.room.WorkoutCallback;
import com.corrot.room.WorkoutsCallback;
import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.db.dao.WorkoutDAO;
import com.corrot.room.db.entity.Workout;

import java.util.Date;
import java.util.List;

public class WorkoutsRepository {

    private WorkoutDAO mWorkoutDAO;
    private LiveData<List<Workout>> mAllWorkouts;
    private LiveData<Workout> mLastWorkout;
    private LiveData<Integer> mTotalCount;

    public WorkoutsRepository(Application application) {
        WorkoutsDatabase db = WorkoutsDatabase.getInstance(application);
        mWorkoutDAO = db.workoutDAO();
        mAllWorkouts = mWorkoutDAO.getAllWorkouts();
        mLastWorkout = mWorkoutDAO.getLastWorkout();
        mTotalCount = mWorkoutDAO.getTotalCount();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return mAllWorkouts;
    }

    public LiveData<Workout> getLastWorkout() {
        return mLastWorkout;
    }

    public LiveData<Integer> getTotalCount() {
        return mTotalCount;
    }

    public void getWorkoutById(String id, WorkoutCallback callback) {
        new getWorkoutByIdAsync(mWorkoutDAO, callback).execute(id);
    }

    public void getWorkoutsByDate(Date date, WorkoutsCallback callback) {
        new getWorkoutByDateAsync(mWorkoutDAO, callback).execute(date);
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

    private static class getWorkoutByIdAsync extends AsyncTask<String, Void, Workout> {

        private final WorkoutDAO workoutDAO;
        private final WorkoutCallback callback;

        getWorkoutByIdAsync(WorkoutDAO dao, WorkoutCallback callback) {
            this.workoutDAO = dao;
            this.callback = callback;
        }

        @Override
        protected Workout doInBackground(String... params) {
            return workoutDAO.getWorkoutById(params[0]);
        }

        @Override
        protected void onPostExecute(Workout workout) {
            callback.onSuccess(workout);
        }
    }

    private static class getWorkoutByDateAsync extends AsyncTask<Date, Void, List<Workout>> {

        private final WorkoutDAO workoutDAO;
        private final WorkoutsCallback callback;

        getWorkoutByDateAsync(WorkoutDAO dao, WorkoutsCallback callback) {
            this.workoutDAO = dao;
            this.callback = callback;
        }

        @Override
        protected List<Workout> doInBackground(Date... params) {
            return workoutDAO.getWorkoutsByDate(params[0]);
        }

        @Override
        protected void onPostExecute(List<Workout> workouts) {
            callback.onSuccess(workouts);
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
