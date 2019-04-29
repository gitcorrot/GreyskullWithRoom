package com.corrot.room.interfaces;

import com.corrot.room.db.entity.Routine;

public interface RoutineCallback {
    void onSuccess(Routine routine);
}
