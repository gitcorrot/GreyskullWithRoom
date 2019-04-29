package com.corrot.room.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.corrot.room.interfaces.ExercisesCallback;
import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.db.dao.ExerciseDAO;
import com.corrot.room.db.entity.Exercise;

import java.util.List;

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

    public LiveData<List<Exercise>> getAllExercisesWithName(String name) {
        return mExerciseDao.getAllExercisesWithName(name);
    }

    public void insertSingleExercise(Exercise exercise) {
        new insertSingleExerciseAsync(mExerciseDao).execute(exercise);
    }

    public void insertMultipleExercises(List<Exercise> exercises) {
        new insertMultipleExercisesAsync(mExerciseDao).execute(exercises);
    }

    public void getExercisesByWorkoutId(String id, ExercisesCallback callback) {
        new getExercisesByWorkoutIdAsync(mExerciseDao, callback).execute(id);
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
            return null;
        }
    }

    private static class insertMultipleExercisesAsync extends AsyncTask<List<Exercise>, Void, Void> {

        private final ExerciseDAO exerciseDAO;

        insertMultipleExercisesAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        // TODO : Handle it
        @Override
        protected Void doInBackground(List<Exercise>... params) {
            exerciseDAO.insertMultipleExercises(params[0]);
            return null;
        }
    }


    private static class getExercisesByWorkoutIdAsync extends AsyncTask<String, Void, List<Exercise>> {

        private final ExerciseDAO exerciseDAO;
        private final ExercisesCallback callback;

        getExercisesByWorkoutIdAsync(ExerciseDAO dao, ExercisesCallback callback) {
            this.exerciseDAO = dao;
            this.callback = callback;
        }

        @Override
        protected List<Exercise> doInBackground(final String... params) {
            return exerciseDAO.getAllExercisesByWorkoutId(params[0]);
        }

        @Override
        protected void onPostExecute(List<Exercise> exercises) {
            callback.onSuccess(exercises);
        }
    }

    private static class deleteAllExercisesByWorkoutIdAsync extends AsyncTask<String, Void, Void> {

        private final ExerciseDAO exerciseDAO;

        deleteAllExercisesByWorkoutIdAsync(ExerciseDAO dao) {
            this.exerciseDAO = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
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
