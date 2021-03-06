package com.corrot.room.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.corrot.room.db.entity.Workout;
import com.corrot.room.interfaces.WorkoutCallback;
import com.corrot.room.interfaces.WorkoutsCallback;
import com.corrot.room.repository.WorkoutsRepository;

import java.util.Date;
import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {

    private WorkoutsRepository mWorkoutsRepository;
    private LiveData<List<Workout>> mAllWorkouts;

    public WorkoutViewModel(Application application) {
        super(application);
        mWorkoutsRepository = new WorkoutsRepository(application);
        mAllWorkouts = mWorkoutsRepository.getAllWorkouts();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return mAllWorkouts;
    }

    public void getWorkoutById(String id, WorkoutCallback callback) {
        mWorkoutsRepository.getWorkoutById(id, callback);
    }

    public void getWorkoutsByDate(Date date, WorkoutsCallback callback) {
        mWorkoutsRepository.getWorkoutsByDate(date, callback);
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
