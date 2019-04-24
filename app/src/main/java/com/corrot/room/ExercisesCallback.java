package com.corrot.room;

import com.corrot.room.db.entity.Exercise;

import java.util.List;

public interface ExercisesCallback {
    void onSuccess(List<Exercise> exercises);
}
