package com.corrot.room.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.db.dao.RoutineDAO;
import com.corrot.room.db.entity.Routine;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class RoutinesRepository {

    private RoutineDAO mRoutineDAO;
    private LiveData<List<Routine>> mAllRoutines;

    public RoutinesRepository(Application application) {
        WorkoutsDatabase db = WorkoutsDatabase.getInstance(application);
        mRoutineDAO = db.routineDAO();
        mAllRoutines = mRoutineDAO.getAllRoutines();
    }

    public LiveData<List<Routine>> getAllRoutines() {
        if (mAllRoutines == null) {
            mAllRoutines = mRoutineDAO.getAllRoutines();
        }
        return mAllRoutines;
    }

    public Routine getRoutineByName(String name)
            throws ExecutionException, InterruptedException {
        return new getRoutineByNameAsync(mRoutineDAO).execute(name).get();
    }

    /*public void deleteAll() {
        new deleteAllAsync(mRoutineDAO).execute();
    }*/

    public void insertSingleRoutine(Routine routine) {
        new insertSingleRoutineAsync(mRoutineDAO).execute(routine);
    }

    public void updateRoutine(Routine routine) {
        new updateRoutineAsync(mRoutineDAO).execute(routine);
    }

    public void deleteRoutine(Routine routine) {
        new deleteRoutineAsync(mRoutineDAO).execute(routine);
    }

    /**********************************************************************************************
     *                                     ASYNC FUNCTIONS
     **********************************************************************************************/

    /*private static class deleteAllAsync extends AsyncTask<Void, Void, Void> {

        private final RoutineDAO workoutDAO;

        deleteAllAsync(RoutineDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workoutDAO.deleteAll();
            return null;
        }
    }*/

    private static class getRoutineByNameAsync extends AsyncTask<String, Void, Routine> {

        private final RoutineDAO routineDAO;

        getRoutineByNameAsync(RoutineDAO dao) {
            this.routineDAO = dao;
        }

        @Override
        protected Routine doInBackground(String... params) {
            return routineDAO.getRoutineByName(params[0]);
        }
    }

    private static class insertSingleRoutineAsync extends AsyncTask<Routine, Void, Void> {

        private final RoutineDAO workoutDAO;

        insertSingleRoutineAsync(RoutineDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(final Routine... params) {
            workoutDAO.insertSingleRoutine(params[0]);
            Log.d("RoutinesRepository", "Routine inserted!");
            return null;
        }
    }

    private static class updateRoutineAsync extends AsyncTask<Routine, Void, Void> {

        private final RoutineDAO routineDAO;

        updateRoutineAsync(RoutineDAO dao) {
            this.routineDAO = dao;
        }

        @Override
        protected Void doInBackground(final Routine... params) {
            routineDAO.updateRoutine(params[0]);
            return null;
        }
    }

    private static class deleteRoutineAsync extends AsyncTask<Routine, Void, Void> {

        private final RoutineDAO workoutDAO;

        deleteRoutineAsync(RoutineDAO dao) {
            this.workoutDAO = dao;
        }

        @Override
        protected Void doInBackground(final Routine... params) {
            workoutDAO.deleteRoutine(params[0]);
            return null;
        }
    }
}
