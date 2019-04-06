package com.corrot.room.viewmodel;

import com.corrot.room.DefinedWorkoutExerciseItem;
import com.corrot.room.repository.NewDefinedWorkoutRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NewDefinedWorkoutViewModel extends ViewModel {

    private NewDefinedWorkoutRepository mNewWorkoutRepository;
    public boolean isChanged;


    public void init() {
        mNewWorkoutRepository = NewDefinedWorkoutRepository.getInstance();
        isChanged = false;
    }

    public void destroyInstance() {
        mNewWorkoutRepository.destroyInstance();
    }

    //------------------------------EXERCISES---------------------------------//

    public LiveData<List<DefinedWorkoutExerciseItem>> getAllExerciseItems() {
        return mNewWorkoutRepository.getAllExercises();
    }

    public void setExercises(List<DefinedWorkoutExerciseItem> exercises) {
        mNewWorkoutRepository.setExercises(exercises);
    }

    public void addExercise(DefinedWorkoutExerciseItem exercise) {
        isChanged = true;
        mNewWorkoutRepository.addExercise(exercise);
    }

    public void updateExercise(DefinedWorkoutExerciseItem exercise) {
        isChanged = true;
        mNewWorkoutRepository.updateExercise(exercise);
    }

    public void removeExercise(DefinedWorkoutExerciseItem exercise) {
        mNewWorkoutRepository.removeExercise(exercise);
        isChanged = true;
    }
}
