package com.corrot.room.repository;

import com.corrot.room.DefinedWorkoutExerciseItem;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NewDefinedWorkoutRepository {

    private static NewDefinedWorkoutRepository instance;
    private MutableLiveData<List<DefinedWorkoutExerciseItem>> mNewExercises;

    public static NewDefinedWorkoutRepository getInstance() {
        if (instance == null) {
            instance = new NewDefinedWorkoutRepository();
        }
        return instance;
    }

    //--------------------------------UTILS----------------------------------//

    public void destroyInstance() {
        instance = null;
    }

    //------------------------------EXERCISES---------------------------------//

    public LiveData<List<DefinedWorkoutExerciseItem>> getAllExercises() {
        if (mNewExercises == null) {
            mNewExercises = new MutableLiveData<>();
        }
        return mNewExercises;
    }

    public void setExercises(List<DefinedWorkoutExerciseItem> exercises) {
        mNewExercises = new MutableLiveData<>();
        mNewExercises.postValue(exercises);
    }

    // add exercise and set its position
    public void addExercise(DefinedWorkoutExerciseItem exercise) {
        List<DefinedWorkoutExerciseItem> items = getAllExercises().getValue();
        if (items != null) {
            exercise.position = items.size();
            items.add(exercise);
            mNewExercises.postValue(items);
        } else {
            items = new ArrayList<>();
            exercise.position = items.size();
            items.add(exercise);
            mNewExercises.postValue(items);
        }
    }

    public void updateExercise(DefinedWorkoutExerciseItem exercise) {
        List<DefinedWorkoutExerciseItem> items = getAllExercises().getValue();
        if (items != null) {
            if (items.get(exercise.position) != null) {
                items.set(exercise.position, exercise);
                mNewExercises.postValue(items);
            }
        }
    }

    public void deleteExercise(DefinedWorkoutExerciseItem exerciseItem) {
        List<DefinedWorkoutExerciseItem> items = getAllExercises().getValue();
        if (items != null) {
            // Remove exercise
            items.remove(exerciseItem);
            // Decrease position of exercises after removed exercise
            for (int i = exerciseItem.position; i < items.size(); i++) {
                items.get(i).position -= 1;
            }
            mNewExercises.postValue(items);
        }
    }
}
