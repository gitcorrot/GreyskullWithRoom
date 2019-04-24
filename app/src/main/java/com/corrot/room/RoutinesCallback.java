package com.corrot.room;

import com.corrot.room.db.entity.Routine;

import java.util.List;

public interface RoutinesCallback {
    void onSuccess(List<Routine> routines);
    void onSuccess(Routine routine);
}
