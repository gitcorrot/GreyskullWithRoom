package com.corrot.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {

    private WorkoutsRepository mWorkoutsRepository;
    private LiveData<List<Workout>> mAllWorkouts;

    public WorkoutViewModel(Application application) {
        super(application);
        mWorkoutsRepository = new WorkoutsRepository(application);
        mAllWorkouts = mWorkoutsRepository.getAllWorkouts();
    }

    LiveData<List<Workout>> getAllWorkouts() {
        return mAllWorkouts;
    }

    public void insertSingleWorkout(Workout workout) {
        mWorkoutsRepository.insertSingleWorkout(workout);
    }

    public void updateWorkout(Workout workout) {
        mWorkoutsRepository.updateWorkout(workout);
    }

    public void deleteWorkout(Workout workout) {
        mWorkoutsRepository.deleteWorkout(workout);
    }

    public void deleteAll() {
        mWorkoutsRepository.deleteAll();
    }

}
