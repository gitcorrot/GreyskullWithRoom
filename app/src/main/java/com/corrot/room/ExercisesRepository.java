package com.corrot.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExercisesRepository {

    private ExerciseDAO mExerciseDao;
    private LiveData<List<Exercise>> mAllExercises;

    public ExercisesRepository(Application application) {
        WorkoutsDatabase db = WorkoutsDatabase.getInstance(application);    // Not sure if it's ok
        mExerciseDao = db.exerciseDAO();
        mAllExercises = mExerciseDao.getAllExercises();
    }

    LiveData<List<Exercise>> getAllExercises() {
        return mAllExercises;
    }

    public void insertSingleExercise(Exercise exercise) {
        new insertSingleExerciseAsync(mExerciseDao).execute(exercise);
    }

   /* public void insertMultipleExercise(List<Exercise> exercises) {
        new insertMultipleExerciseAsync(mExerciseDao).execute(exercises);
    }*/

    public LiveData<List<Exercise>> getAllExercisesByWorkoutId(long id)
            throws ExecutionException, InterruptedException {
        return new getAllExercisesByWorkoutIdAsync(mExerciseDao).execute(id).get();
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


    private static class getAllExercisesByWorkoutIdAsync extends AsyncTask<Long, Void, LiveData<List<Exercise>>> {

        private final ExerciseDAO exerciseDAO;

        getAllExercisesByWorkoutIdAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected LiveData<List<Exercise>> doInBackground(final Long... params) {
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
