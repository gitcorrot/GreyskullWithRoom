package com.corrot.room.viewmodel;

import android.app.Application;

import com.corrot.room.db.entity.DefinedWorkout;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.repository.DefinedWorkoutsRepository;
import com.corrot.room.repository.WorkoutsRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DefinedWorkoutViewModel extends AndroidViewModel {

    private DefinedWorkoutsRepository mDefinedWorkoutsRepository;
    private LiveData<List<DefinedWorkout>> mAllWorkouts;

    public DefinedWorkoutViewModel(Application application) {
        super(application);
        mDefinedWorkoutsRepository = new DefinedWorkoutsRepository(application);
        mAllWorkouts = mDefinedWorkoutsRepository.getAllWorkouts();
    }

    public LiveData<List<DefinedWorkout>> getAllWorkouts() {
        return mAllWorkouts;
    }

    public void insertSingleWorkout(DefinedWorkout workout) {
        mDefinedWorkoutsRepository.insertSingleWorkout(workout);
    }

    public void updateWorkout(DefinedWorkout workout) {
        mDefinedWorkoutsRepository.updateWorkout(workout);
    }

    public void deleteWorkout(DefinedWorkout workout) {
        mDefinedWorkoutsRepository.deleteWorkout(workout);
    }

    public void deleteAll() {
        mDefinedWorkoutsRepository.deleteAll();
    }
}
