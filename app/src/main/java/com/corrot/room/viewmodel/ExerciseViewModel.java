package com.corrot.room.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.corrot.room.ExercisesCallback;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.repository.ExercisesRepository;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {

    private ExercisesRepository mExercisesRepository;
    private LiveData<List<Exercise>> mAllExercises;
    private LiveData<List<Exercise>> mAllFilteredExercises;
    private MutableLiveData<String> exerciseNameFilter = new MutableLiveData<>();

    public ExerciseViewModel(Application application) {
        super(application);
        mExercisesRepository = new ExercisesRepository(application);
        mAllExercises = mExercisesRepository.getAllExercises();
        mAllFilteredExercises = Transformations.switchMap(exerciseNameFilter, name ->
                mExercisesRepository.getAllExercisesWithName(name)
        );
    }

    public void setName(String name) {
        exerciseNameFilter.setValue(name);
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return mAllExercises;
    }

    // TODO: Use Transformations.switchMap.
    public LiveData<List<Exercise>> getAllExercisesWithName() {
        return mAllFilteredExercises;
    }
    /*public void getAllExercisesWithName(String name, ExercisesCallback callback) {
        mExercisesRepository.getAllExercisesWithName(name, callback);
    }*/

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
