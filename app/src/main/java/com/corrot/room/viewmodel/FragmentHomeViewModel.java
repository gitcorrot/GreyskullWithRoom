package com.corrot.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.corrot.room.db.entity.Workout;
import com.corrot.room.repository.WorkoutsRepository;

import java.util.Date;
import java.util.List;

public class FragmentHomeViewModel extends AndroidViewModel {

    private WorkoutsRepository mWorkoutsRepository;
    private MediatorLiveData<List<Workout>> mWorkoutsFromTo = new MediatorLiveData<>();
    private MutableLiveData<Date> dateFrom = new MutableLiveData<>();
    private MutableLiveData<Date> dateTo = new MutableLiveData<>();

    public FragmentHomeViewModel(@NonNull Application application) {
        super(application);
        mWorkoutsRepository = new WorkoutsRepository(application);

        mWorkoutsFromTo.addSource(dateFrom, from -> {
            if (dateTo.getValue() != null) {
                mWorkoutsRepository.getWorkoutsFromTo(from, dateTo.getValue(),
                        workouts -> mWorkoutsFromTo.setValue(workouts));
            }
        });

        mWorkoutsFromTo.addSource(dateTo, to -> {
            if (dateFrom.getValue() != null) {
                mWorkoutsRepository.getWorkoutsFromTo(dateFrom.getValue(), to, workouts ->
                        mWorkoutsFromTo.setValue(workouts));
            }
        });
    }

    public LiveData<List<Workout>> getWorkoutsFromTo() {
        return mWorkoutsFromTo;
    }

    public void setDateFrom(Date d) {
        dateFrom.setValue(d);
    }

    public void setDateTo(Date d) {
        dateTo.setValue(d);
    }
}
