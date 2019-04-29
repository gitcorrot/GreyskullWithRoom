package com.corrot.room.interfaces;

import com.corrot.room.db.entity.Workout;

import java.util.List;

public interface WorkoutsCallback {
    void onSuccess(List<Workout> workouts);
}
