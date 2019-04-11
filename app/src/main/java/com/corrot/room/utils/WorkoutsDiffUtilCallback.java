package com.corrot.room.utils;

import com.corrot.room.db.entity.Workout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class WorkoutsDiffUtilCallback extends DiffUtil.Callback {

    private List<Workout> oldList;
    private List<Workout> newList;

    public WorkoutsDiffUtilCallback(List<Workout> oldList, List<Workout> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldList.get(oldItemPosition).id.equals(newList.get(newItemPosition).id));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        // check if exercises are the same
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
