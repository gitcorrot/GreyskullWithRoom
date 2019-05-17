package com.corrot.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.corrot.room.db.entity.Routine;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.repository.RoutinesRepository;
import com.corrot.room.repository.WorkoutsRepository;
import com.corrot.room.utils.MyTimeUtils;

import java.util.Date;
import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {

    private final static int loadIncrease = 5;
    private WorkoutsRepository mWorkoutsRepository;

    private MutableLiveData<Integer> loadCount = new MutableLiveData<>();
    private MediatorLiveData<List<Workout>> mRecentWorkouts = new MediatorLiveData<>();
    private LiveData<List<Routine>> mAllRoutines;
    private LiveData<String> mLastWorkout;
    private LiveData<String> mTotalCount;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        mWorkoutsRepository = new WorkoutsRepository(application);
        RoutinesRepository mRoutinesRepository = new RoutinesRepository(application);
        loadCount.setValue(loadIncrease);
        mAllRoutines = mRoutinesRepository.getAllRoutines();

        mTotalCount = Transformations.map(mWorkoutsRepository.getTotalCount(), count -> {
            String sb;
            if (count != null) {
                if (count > 0) sb = "Total number of workouts: " + count;
                else sb = null;
            } else sb = null;
            return sb;
        });

        mLastWorkout = Transformations.map(mWorkoutsRepository.getLastWorkout(), workout -> {
            if (workout != null) {
                String date = MyTimeUtils.parseDate(workout.workoutDate, MyTimeUtils.MAIN_FORMAT);
                return "Your last workout: " + date;
            } else return "Add your first workout!";
        });

        mRecentWorkouts.addSource(loadCount, count ->
                mWorkoutsRepository.getRecentWorkouts(count, workouts ->
                mRecentWorkouts.setValue(workouts)));

        mRecentWorkouts.addSource(mWorkoutsRepository.getAllWorkouts(), allWorkouts -> {
            if (loadCount.getValue() != null) {
                mWorkoutsRepository.getRecentWorkouts(loadCount.getValue(), workouts ->
                        mRecentWorkouts.setValue(workouts));
            }
        });
    }

    // Workouts

    public LiveData<List<Workout>> getRecentWorkouts() {
        return mRecentWorkouts;
    }

    public void increaseLoadCount() {
        if (loadCount.getValue() != null) {
            int c = loadCount.getValue();
            loadCount.postValue(c + loadIncrease);
        }
    }

    public LiveData<String> getLastWorkout() {
        return mLastWorkout;
    }

    public LiveData<String> getTotalCount() {
        return mTotalCount;
    }

    public void deleteWorkout(Workout workout) {
        mWorkoutsRepository.deleteWorkout(workout);
    }

    // Routines

    public LiveData<List<Routine>> getAllRoutines() {
        return mAllRoutines;
    }

}
