package com.corrot.room.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.corrot.room.repository.WorkoutsRepository;
import com.corrot.room.db.entity.Workout;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public Workout getWorkoutById(String id)
        throws ExecutionException, InterruptedException {
        return mWorkoutsRepository.getWorkoutById(id);
    }

    public List<Workout> getWorkoutsByDate(Date date)
            throws ExecutionException, InterruptedException {
        return mWorkoutsRepository.getWorkoutsByDate(date);
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
