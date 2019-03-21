package com.corrot.room.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.repository.NewWorkoutRepository;

import java.util.List;

public class NewWorkoutViewModel extends ViewModel {

    private NewWorkoutRepository mNewWorkoutRepository;

    public void init() {
        mNewWorkoutRepository = NewWorkoutRepository.getInstance();
    }

    //------------------------------EXERCISES---------------------------------//

    public LiveData<List<ExerciseItem>> getAllExerciseItems() {
        return mNewWorkoutRepository.getAllExercises();
    }

    public void addExercise(ExerciseItem exerciseItem) {
        mNewWorkoutRepository.addExercise(exerciseItem);
    }

    //--------------------------------SETS-----------------------------------//

    public LiveData<List<ExerciseSetItem>> getAllSetItems() {
        return mNewWorkoutRepository.getAllSets();
    }

    public List<ExerciseSetItem> getSetsByExerciseId(int exerciseId) {
        return mNewWorkoutRepository.getSetsByExerciseId(exerciseId);
    }

    public void addSet(ExerciseSetItem setItem) {
        mNewWorkoutRepository.addSet(setItem);
    }

    public void updateSet(ExerciseSetItem setItem, int position) {
        mNewWorkoutRepository.updateSet(setItem, position);
    }
}
