package com.corrot.room.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.dao.ExerciseDAO;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExercisesRepository {

    private ExerciseDAO mExerciseDao;
    private LiveData<List<Exercise>> mAllExercises;

    public ExercisesRepository(Application application) {
        WorkoutsDatabase db = WorkoutsDatabase.getInstance(application);
        mExerciseDao = db.exerciseDAO();
        mAllExercises = mExerciseDao.getAllExercises();
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return mAllExercises;
    }

    public void insertSingleExercise(Exercise exercise) {
        new insertSingleExerciseAsync(mExerciseDao).execute(exercise);
    }

   /* public void insertMultipleExercises(List<Exercise> exercises) {
        new insertMultipleExerciseAsync(mExerciseDao).execute(exercises);
    }*/

    public List<Exercise> getExercisesByWorkoutId(String id)
            throws ExecutionException, InterruptedException {
        return new getExercisesByWorkoutIdAsync(mExerciseDao).execute(id).get();
    }

    public void updateSingleExercise(Exercise exercise) {
        new updateSingleExerciseAsync(mExerciseDao).execute(exercise);
    }

    public void deleteSingleExercise(Exercise exercise) {
        new deleteSingleExerciseAsync(mExerciseDao).execute(exercise);
    }

    /***********************************************************************************************
    *                                       ASYNC FUNCTIONS
    ***********************************************************************************************/


    private static class insertSingleExerciseAsync extends AsyncTask<Exercise, Void, Void> {

        private final ExerciseDAO exerciseDAO;

        insertSingleExerciseAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            exerciseDAO.insertSingleExercise(params[0]);
            Log.d("asdasd", "Exercise inserted!");
            return null;
        }
    }

    /*private static class insertMultipleExerciseAsync extends AsyncTask<List<Exercise>, Void, Void> {

        private final ExerciseDAO exerciseDAO;

        insertMultipleExerciseAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            exerciseDAO.insertSingleExercise(params[0]);
            return null;
        }
    }*/


    private static class getExercisesByWorkoutIdAsync extends AsyncTask<String, Void, List<Exercise>> {

        private final ExerciseDAO exerciseDAO;

        getExercisesByWorkoutIdAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected  List<Exercise> doInBackground(final String... params) {
            return exerciseDAO.getAllExercisesByWorkoutId(params[0]);
        }
    }

    private static class updateSingleExerciseAsync extends AsyncTask<Exercise, Void, Void> {

        private final ExerciseDAO exerciseDAO;

        updateSingleExerciseAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            exerciseDAO.updateExercise(params[0]);
            return null;
        }
    }

    private static class deleteSingleExerciseAsync extends AsyncTask<Exercise, Void, Void> {

        private final ExerciseDAO exerciseDAO;

        deleteSingleExerciseAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            exerciseDAO.deleteExercise(params[0]);
            return null;
        }
    }
}
