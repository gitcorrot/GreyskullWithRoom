package com.corrot.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.repository.ExercisesRepository;
import com.corrot.room.repository.WorkoutsRepository;

import java.util.Date;
import java.util.List;

public class StatsFragmentViewModel extends AndroidViewModel {

    private ExercisesRepository mExercisesRepository;
    private LiveData<List<Exercise>> mAllFilteredExercises;
    private MutableLiveData<String> exerciseNameFilter = new MutableLiveData<>();

    public StatsFragmentViewModel(@NonNull Application application) {
        super(application);
        mExercisesRepository = new ExercisesRepository(application);

        mAllFilteredExercises = Transformations.switchMap(exerciseNameFilter, name ->
                mExercisesRepository.getAllExercisesWithName(name)
        );
    }

    public void setName(String name) {
        exerciseNameFilter.setValue(name);
    }

    public LiveData<List<Exercise>> getAllExercisesWithName() {
        return mAllFilteredExercises;
    }
}
