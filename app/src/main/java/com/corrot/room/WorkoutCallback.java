package com.corrot.room;

import com.corrot.room.db.entity.Workout;

import java.util.List;

public interface WorkoutCallback {
    void onSuccess(Workout workout);
}
