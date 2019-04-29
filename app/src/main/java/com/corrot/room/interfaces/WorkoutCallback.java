package com.corrot.room.interfaces;

import com.corrot.room.db.entity.Workout;

import java.util.List;

public interface WorkoutCallback {
    void onSuccess(Workout workout);
}
