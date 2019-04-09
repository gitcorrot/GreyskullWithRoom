package com.corrot.room.viewmodel;

import android.app.Application;

import com.corrot.room.db.entity.Routine;
import com.corrot.room.repository.RoutinesRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RoutineViewModel extends AndroidViewModel {

    private RoutinesRepository mRoutinesRepository;
    private LiveData<List<Routine>> mAllRoutines;

    public RoutineViewModel(Application application) {
        super(application);
        mRoutinesRepository = new RoutinesRepository(application);
        mAllRoutines = mRoutinesRepository.getAllRoutines();
    }

    public LiveData<List<Routine>> getAllRoutines() {
        return mAllRoutines;
    }

    public void insertSingleRoutine(Routine routine) {
        mRoutinesRepository.insertSingleRoutine(routine);
    }

    public void updateRoutine(Routine routine) {
        mRoutinesRepository.updateRoutine(routine);
    }

    public void deleteRoutine(Routine routine) {
        mRoutinesRepository.deleteRoutine(routine);
    }

    public void deleteAll() {
        mRoutinesRepository.deleteAll();
    }
}
