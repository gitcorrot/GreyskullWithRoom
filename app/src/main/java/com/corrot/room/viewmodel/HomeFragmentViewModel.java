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

    private WorkoutsRepository mWorkoutsRepository;

    private MediatorLiveData<List<Workout>> mWorkoutsFromTo = new MediatorLiveData<>();
    private MutableLiveData<Date> dateFrom = new MutableLiveData<>();
    private MutableLiveData<Date> dateTo = new MutableLiveData<>();
    private LiveData<String> mLastWorkout;
    private LiveData<String> mTotalCount;

    private LiveData<List<Routine>> mAllRoutines;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        mWorkoutsRepository = new WorkoutsRepository(application);
        RoutinesRepository mRoutinesRepository = new RoutinesRepository(application);

        mAllRoutines = mRoutinesRepository.getAllRoutines();

        mTotalCount = Transformations.map(mWorkoutsRepository.getTotalCount(), count -> {
            String sb;
            if (count != null) sb = "Total number of workouts: " + count;
            else sb = "Total number of workouts: 0";
            return sb;
        });

        mLastWorkout = Transformations.map(mWorkoutsRepository.getLastWorkout(), workout -> {
            if (workout != null) {
                String date = MyTimeUtils.parseDate(workout.workoutDate, MyTimeUtils.MAIN_FORMAT);
                return "Your last workout: " + date;
            } else return "Add your first workout!";
        });

        mWorkoutsFromTo.addSource(dateFrom, from -> {
            Date to = dateTo.getValue();
            if (to != null) {
                mWorkoutsRepository.getWorkoutsFromTo(from, to, workouts ->
                        mWorkoutsFromTo.setValue(workouts));
            }
        });

        mWorkoutsFromTo.addSource(dateTo, to -> {
            Date from = dateFrom.getValue();
            if (from != null) {
                mWorkoutsRepository.getWorkoutsFromTo(from, to, workouts ->
                        mWorkoutsFromTo.setValue(workouts));
            }
        });

        mWorkoutsFromTo.addSource(mWorkoutsRepository.getAllWorkouts(), allWorkouts -> {
            Date from = dateFrom.getValue();
            Date to = dateTo.getValue();
            if (from != null && to != null) {
                mWorkoutsRepository.getWorkoutsFromTo(from, to, workouts ->
                        mWorkoutsFromTo.setValue(workouts));
            }
        });
    }

    // Workouts

    public LiveData<List<Workout>> getWorkoutsFromTo() {
        return mWorkoutsFromTo;
    }

    public void setDateFrom(Date d) {
        dateFrom.setValue(d);
    }

    public void setDateTo(Date d) {
        dateTo.setValue(d);
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
