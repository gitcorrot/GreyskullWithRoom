package com.corrot.room.viewmodel;

import com.corrot.room.RoutineExerciseItem;
import com.corrot.room.repository.NewRoutineRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NewRoutineViewModel extends ViewModel {

    private NewRoutineRepository mNewWorkoutRepository;

    public void init() {
        mNewWorkoutRepository = NewRoutineRepository.getInstance();
    }

    public void destroyInstance() {
        mNewWorkoutRepository.destroyInstance();
    }

    //------------------------------EXERCISES---------------------------------//

    public LiveData<List<RoutineExerciseItem>> getAllExerciseItems() {
        return mNewWorkoutRepository.getAllExercises();
    }

    public void setExercises(List<RoutineExerciseItem> exercises) {
        mNewWorkoutRepository.setExercises(exercises);
    }

    public void addExercise(RoutineExerciseItem exercise) {
        mNewWorkoutRepository.addExercise(exercise);
    }

    public void deleteExercise(RoutineExerciseItem exercise) {
        mNewWorkoutRepository.deleteExercise(exercise);
    }

    public void updateExercise(RoutineExerciseItem exercise) {
        mNewWorkoutRepository.updateExercise(exercise);
    }
}
