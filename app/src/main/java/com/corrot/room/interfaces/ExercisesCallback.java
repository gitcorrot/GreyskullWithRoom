package com.corrot.room.interfaces;

import com.corrot.room.db.entity.Exercise;

import java.util.List;

public interface ExercisesCallback {
    void onSuccess(List<Exercise> exercises);
}
