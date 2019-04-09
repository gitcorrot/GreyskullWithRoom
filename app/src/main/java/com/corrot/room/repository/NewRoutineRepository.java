package com.corrot.room.repository;

import com.corrot.room.RoutineExerciseItem;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NewRoutineRepository {

    private static NewRoutineRepository instance;
    private MutableLiveData<List<RoutineExerciseItem>> mNewExercises;

    public static NewRoutineRepository getInstance() {
        if (instance == null) {
            instance = new NewRoutineRepository();
        }
        return instance;
    }

    //--------------------------------UTILS----------------------------------//

    public void destroyInstance() {
        instance = null;
    }

    //------------------------------EXERCISES---------------------------------//

    public LiveData<List<RoutineExerciseItem>> getAllExercises() {
        if (mNewExercises == null) {
            mNewExercises = new MutableLiveData<>();
        }
        return mNewExercises;
    }

    public void setExercises(List<RoutineExerciseItem> exercises) {
        mNewExercises = new MutableLiveData<>();
        mNewExercises.postValue(exercises);
    }

    // add exercise and set its position
    public void addExercise(RoutineExerciseItem exercise) {
        List<RoutineExerciseItem> items = getAllExercises().getValue();
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

    public void updateExercise(RoutineExerciseItem exercise) {
        List<RoutineExerciseItem> items = getAllExercises().getValue();
        if (items != null && items.size() >= exercise.position) {
            if (items.get(exercise.position) != null) {
                items.set(exercise.position, exercise);
                mNewExercises.postValue(items);
            }
        } else {
            items = new ArrayList<>();
            items.add(exercise);
            mNewExercises.postValue(items);
        }
    }

    public void deleteExercise(RoutineExerciseItem exerciseItem) {
        List<RoutineExerciseItem> items = getAllExercises().getValue();
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
