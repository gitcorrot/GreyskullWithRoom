package com.corrot.room.utils;

import com.corrot.room.db.entity.DefinedWorkout;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class DefinedWorkoutsDiffUtilCallback extends DiffUtil.Callback {

    private List<DefinedWorkout> oldList;
    private List<DefinedWorkout> newList;

    public DefinedWorkoutsDiffUtilCallback(List<DefinedWorkout> oldList, List<DefinedWorkout> newList) {
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
        return (oldList.get(oldItemPosition).id == newList.get(newItemPosition).id);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).label.equals(newList.get(newItemPosition).label)
                && oldList.get(oldItemPosition).exercises.equals(newList.get(newItemPosition).exercises);
    }
}
