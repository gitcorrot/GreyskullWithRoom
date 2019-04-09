package com.corrot.room.utils;

import com.corrot.room.db.entity.Routine;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class RoutinesDiffUtilCallback extends DiffUtil.Callback {

    private List<Routine> oldList;
    private List<Routine> newList;

    public RoutinesDiffUtilCallback(List<Routine> oldList, List<Routine> newList) {
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
