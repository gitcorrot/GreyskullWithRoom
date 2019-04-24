package com.corrot.room.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
        if (instance == null) {
            instance = new NewWorkoutRepository();
        }
        return instance;
    }

    //--------------------------------UTILS----------------------------------//

    public void destroyInstance() {
        instance = null;
    }

    //------------------------------EXERCISES---------------------------------//

    public LiveData<List<ExerciseItem>> getAllExercises() {
        if (mNewExercises == null) {
            mNewExercises = new MutableLiveData<>();
        }
        return mNewExercises;
    }

    public void setExercises(List<ExerciseItem> exercises) {
        if (mNewExercises == null) {
            mNewExercises = new MutableLiveData<>();
        }
        mNewExercises.setValue(exercises);
    }

    public ExerciseItem getExerciseByPosition(int position) {
        List<ExerciseItem> exercises = getAllExercises().getValue();
        if (exercises != null) {
            for (ExerciseItem e : exercises) {
                if (e.position == position)
                    return e;
            }
        }
        return null;
    }

    // add exercise and set its ID
    public void addExercise(ExerciseItem exerciseItem) {
        List<ExerciseItem> items = getAllExercises().getValue();
        if (items != null) {
            exerciseItem.position = items.size();
            items.add(exerciseItem);
            mNewExercises.postValue(items);
        } else {
            items = new ArrayList<>();
            exerciseItem.position = items.size();
            items.add(exerciseItem);
            mNewExercises.postValue(items);
        }
    }

    public void removeExercise(ExerciseItem exerciseItem) {

        // not only remove. Also change all next exercises and sets ids

        List<ExerciseItem> items = getAllExercises().getValue();
        if (items != null) {

            // Remove sets of exercise
            List<ExerciseSetItem> sets = getSetsByExercisePosition(exerciseItem.position);
            removeMultipleSets(sets);

            // Remove exercise
            items.remove(exerciseItem);

            // Decrease id of exercises after removed exercise position
            for (int i = exerciseItem.position; i < items.size(); i++) {
                items.get(i).position -= 1;
            }
            mNewExercises.postValue(items);

            // Decrease id of exercises' sets after removed exercise position
            List<ExerciseSetItem> setItems = getAllSets().getValue();
            if (setItems != null) {
                for (ExerciseSetItem s : setItems) {
                    if (s.exercisePosition > exerciseItem.position)
                        s.exercisePosition -= 1;
                }
                mNewSets.postValue(setItems);
            }
        }
    }

    //--------------------------------SETS-----------------------------------//

    public LiveData<List<ExerciseSetItem>> getAllSets() {
        if (mNewSets == null) {
            mNewSets = new MutableLiveData<>();
        }
        return mNewSets;
    }

    public void setSets(List<ExerciseSetItem> sets) {
        if (mNewSets == null) {
            mNewSets = new MutableLiveData<>();
        }
        mNewSets.setValue(sets);
    }

    public List<ExerciseSetItem> getSetsByExercisePosition(int position) {
        List<ExerciseSetItem> setItems = getAllSets().getValue();
        List<ExerciseSetItem> exerciseSetItems = new ArrayList<>();
        if (setItems != null) {
            for (ExerciseSetItem e : setItems) {
                if (e.exercisePosition == position) exerciseSetItems.add(e);
            }
        }
        return exerciseSetItems;
    }

    public void addSet(ExerciseSetItem setItem) {
        List<ExerciseSetItem> items = getAllSets().getValue();
        if (items != null) {
            items.add(setItem);
            mNewSets.postValue(items);
        } else {
            items = new ArrayList<>();
            items.add(setItem);
            mNewSets.postValue(items);
        }
    }

    public void updateSet(ExerciseSetItem setItem, int position) {
        List<ExerciseSetItem> items = getAllSets().getValue();
        List<ExerciseSetItem> exerciseSetItems = getSetsByExercisePosition(setItem.exercisePosition);

        // update set on position
        if (exerciseSetItems != null) {
            exerciseSetItems.set(position, setItem);
        } else {
            Log.e("NewWorkoutRepository", "Error when updating set. List of set is null!");
        }

        // delete all sets with setItem.exerciseId
        // add updated list of sets to sets
        if (items != null && exerciseSetItems != null) {
            List<ExerciseSetItem> toRemove = new ArrayList<>();


            for (ExerciseSetItem e : items) {
                if (e.exercisePosition == setItem.exercisePosition) toRemove.add(e);
            }

            items.removeAll(toRemove);
            items.addAll(exerciseSetItems);
        }
        mNewSets.postValue(items);
    }

    public void removeSet(ExerciseSetItem setItem) {
        List<ExerciseSetItem> items = getAllSets().getValue();
        if (items != null) {
            items.remove(setItem);
            mNewSets.postValue(items);
        }
    }

    private void removeMultipleSets(List<ExerciseSetItem> setItems) {
        List<ExerciseSetItem> items = getAllSets().getValue();
        if (items != null) {
            items.removeAll(setItems);
            mNewSets.postValue(items);
        }
    }
}
