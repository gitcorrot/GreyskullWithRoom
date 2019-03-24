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

    public ExerciseItem getExerciseById(int id) {
        return mNewWorkoutRepository.getExerciseById(id);
    }

    public void addExercise(ExerciseItem exerciseItem) {
        mNewWorkoutRepository.addExercise(exerciseItem);
    }

    public void removeExercise(ExerciseItem exerciseItem) {
        mNewWorkoutRepository.removeExercise(exerciseItem);
    }

    //--------------------------------SETS-----------------------------------//

    public LiveData<List<ExerciseSetItem>> getAllSetItems() {
        return mNewWorkoutRepository.getAllSets();
    }

    public void setSets(List<ExerciseSetItem> sets) {
        mNewWorkoutRepository.setSets(sets);
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

    public void removeSet(ExerciseSetItem setItem) {
        mNewWorkoutRepository.removeSet(setItem);
    }
}
