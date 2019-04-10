package com.corrot.room.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.repository.NewWorkoutRepository;

import java.util.List;

public class NewWorkoutViewModel extends ViewModel {

    private NewWorkoutRepository mNewWorkoutRepository;
    public boolean isChanged;

    public void init() {
        mNewWorkoutRepository = NewWorkoutRepository.getInstance();
        isChanged = false;
    }

    public void destroyInstance() {
        mNewWorkoutRepository.destroyInstance();
    }

    //------------------------------EXERCISES---------------------------------//

    public LiveData<List<ExerciseItem>> getAllExerciseItems() {
        return mNewWorkoutRepository.getAllExercises();
    }

    public void setExercises(List<ExerciseItem> exercises) {
        mNewWorkoutRepository.setExercises(exercises);
    }

    public ExerciseItem getExerciseByPosition(int position) {
        return mNewWorkoutRepository.getExerciseByPosition(position);
    }

    public void addExercise(ExerciseItem exerciseItem) {
        isChanged = true;
        mNewWorkoutRepository.addExercise(exerciseItem);
    }

    public void removeExercise(ExerciseItem exerciseItem) {
        mNewWorkoutRepository.removeExercise(exerciseItem);
        isChanged = true;
    }

    //--------------------------------SETS-----------------------------------//

    public LiveData<List<ExerciseSetItem>> getAllSetItems() {
        return mNewWorkoutRepository.getAllSets();
    }

    public void setSets(List<ExerciseSetItem> sets) {
        mNewWorkoutRepository.setSets(sets);
    }

    public List<ExerciseSetItem> getSetsByExercisePosition(int exercisePosition) {
        return mNewWorkoutRepository.getSetsByExercisePosition(exercisePosition);
    }

    public void addSet(ExerciseSetItem setItem) {
        mNewWorkoutRepository.addSet(setItem);
        isChanged = true;
    }

    public void updateSet(ExerciseSetItem setItem, int position) {
        mNewWorkoutRepository.updateSet(setItem, position);
        isChanged = true;
    }

    public void removeSet(ExerciseSetItem setItem) {
        mNewWorkoutRepository.removeSet(setItem);
        isChanged = true;
    }
}
