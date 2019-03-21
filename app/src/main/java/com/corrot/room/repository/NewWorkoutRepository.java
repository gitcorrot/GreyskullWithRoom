package com.corrot.room.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;

import java.util.ArrayList;
import java.util.List;

public class NewWorkoutRepository {

    private static NewWorkoutRepository instance;
    private MutableLiveData<List<ExerciseItem>> mNewExercises;
    private MutableLiveData<List<ExerciseSetItem>> mNewSets;

    public static NewWorkoutRepository getInstance() {
        if(instance == null) {
            instance = new NewWorkoutRepository();
        }
        return instance;
    }

    //------------------------------EXERCISES---------------------------------//

    public LiveData<List<ExerciseItem>> getAllExercises() {
        if(mNewExercises == null) {
            mNewExercises = new MutableLiveData<>();
        }
        return mNewExercises;
    }

    // add exercise and set its ID
    public void addExercise(ExerciseItem exerciseItem) {
        List<ExerciseItem> items = getAllExercises().getValue();
        if(items != null) {
            exerciseItem.id = items.size();
            Log.d("asdasd", "Exercise (ID: " + exerciseItem.id
                    + " added successfully! " + "List.size() == " + items.size());
            items.add(exerciseItem);
            mNewExercises.postValue(items);
        }
        else {
            items = new ArrayList<>();
            exerciseItem.id = items.size();
            Log.d("asdasd", "ExercisesList == null. Creating exercises list!");
            items.add(exerciseItem);
            mNewExercises.postValue(items);
        }
    }

    //--------------------------------SETS-----------------------------------//

    public LiveData<List<ExerciseSetItem>> getAllSets() {
        if(mNewSets == null) {
            mNewSets = new MutableLiveData<>();
        }
        return mNewSets;
    }

    public List<ExerciseSetItem> getSetsByExerciseId(int id) {
        List<ExerciseSetItem> setItems = getAllSets().getValue();
        List<ExerciseSetItem> exerciseSetItems = new ArrayList<>();
        if(setItems != null) {
            for(ExerciseSetItem e : setItems) {
                if(e.exerciseId == id) exerciseSetItems.add(e);
            }
        }
        return exerciseSetItems;
    }

    public void addSet(ExerciseSetItem setItem) {
        List<ExerciseSetItem> items = getAllSets().getValue();
        if(items != null) {
            items.add(setItem);
            Log.d("asdasd", "GetAllSets != null, ID: " + setItem.exerciseId + " List.size() == " + items.size());
            mNewSets.postValue(items);
        }
        else {
            Log.d("asdasd", "GetAllSets == null, creating new ArrayList");
            items = new ArrayList<>();
            items.add(setItem);
            mNewSets.postValue(items);
        }
    }

    public void updateSet(ExerciseSetItem setItem, int position) {
        List<ExerciseSetItem> items = getAllSets().getValue();
        List<ExerciseSetItem> exerciseSetItems = getSetsByExerciseId(setItem.exerciseId);

        // update set on position
        if(exerciseSetItems != null) {
            exerciseSetItems.set(position, setItem);
        }
        else {
            Log.e("asdasd", "Error when updating set. List of set is null!");
        }

        // delete all sets with setItem.exerciseId
        // add updated list of sets to sets
        if(items != null && exerciseSetItems != null) {
            List<ExerciseSetItem> toRemove = new ArrayList<>();


            for (ExerciseSetItem e : items) {
                if(e.exerciseId == setItem.exerciseId) toRemove.add(e);
            }

            items.removeAll(toRemove);
            items.addAll(exerciseSetItems);
        }
        Log.d("asdasd", "Set on position " + position + " updated successfully!");
        mNewSets.postValue(items);
    }
}
