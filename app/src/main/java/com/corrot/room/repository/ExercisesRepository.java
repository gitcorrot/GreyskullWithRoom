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

    public List<Exercise> getAllExercises(String name)
            throws ExecutionException, InterruptedException {
        return new getAllExercisesAsync(mExerciseDao).execute(name).get();
    }

    public void insertSingleExercise(Exercise exercise) {
        new insertSingleExerciseAsync(mExerciseDao).execute(exercise);
    }

    public void insertMultipleExercises(List<Exercise> exercises) {
        new insertMultipleExercisesAsync(mExerciseDao).execute(exercises);
    }

    public List<Exercise> getExercisesByWorkoutId(String id)
            throws ExecutionException, InterruptedException {
        return new getExercisesByWorkoutIdAsync(mExerciseDao).execute(id).get();
    }

    public void deleteAllExercisesByWorkoutId(String id) {
        new deleteAllExercisesByWorkoutIdAsync(mExerciseDao).execute(id);
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

    private static class insertMultipleExercisesAsync extends AsyncTask<List<Exercise>, Void, Void> {

        private final ExerciseDAO exerciseDAO;

        insertMultipleExercisesAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected Void doInBackground(List<Exercise>... params) {
            exerciseDAO.insertMultipleExercises(params[0]);
            return null;
        }
    }


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

    private static class getAllExercisesAsync extends AsyncTask<String, Void, List<Exercise>> {

        private final ExerciseDAO exerciseDAO;

        getAllExercisesAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected  List<Exercise> doInBackground(final String... params) {
            return exerciseDAO.getAllExercises(params[0]);
        }
    }

    private static class deleteAllExercisesByWorkoutIdAsync extends AsyncTask<String, Void, Void> {

        private final ExerciseDAO exerciseDAO;

        deleteAllExercisesByWorkoutIdAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected  Void doInBackground(final String... params) {
            exerciseDAO.deleteAllExercisesByWorkoutId(params[0]);
            return null;
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
