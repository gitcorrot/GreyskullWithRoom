package com.corrot.room.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.corrot.room.ExercisesCallback;
import com.corrot.room.repository.ExercisesRepository;
import com.corrot.room.db.entity.Exercise;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseViewModel extends AndroidViewModel {

    private ExercisesRepository mExercisesRepository;
    private LiveData<List<Exercise>> mAllExercises;

    public ExerciseViewModel(Application application) {
        super(application);
        mExercisesRepository = new ExercisesRepository(application);
        mAllExercises = mExercisesRepository.getAllExercises();
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return mAllExercises;
    }

    public List<Exercise> getAllExercisesWithName(String name)
            throws ExecutionException, InterruptedException {
        return mExercisesRepository.getAllExercisesWithName(name);
    }

    public void insertSingleExercise(Exercise exercise) {
        mExercisesRepository.insertSingleExercise(exercise);
    }

    public void insertMultipleExercises(List<Exercise> exercises) {
        mExercisesRepository.insertMultipleExercises(exercises);
    }

    public void getExercisesByWorkoutId(String id, ExercisesCallback callback) {
        mExercisesRepository.getExercisesByWorkoutId(id, callback);
    }

    public void deleteAllExercisesByWorkoutId(String id) {
        mExercisesRepository.deleteAllExercisesByWorkoutId(id);
    }

    public void updateSingleExercise(Exercise exercise) {
        mExercisesRepository.updateSingleExercise(exercise);
    }

    public void deleteSingleExercise(Exercise exercise) {
        mExercisesRepository.deleteSingleExercise(exercise);
    }
}
