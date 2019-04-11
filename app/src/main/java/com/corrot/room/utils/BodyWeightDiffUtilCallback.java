package com.corrot.room.utils;

import com.corrot.room.BodyWeightItem;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class BodyWeightDiffUtilCallback extends DiffUtil.Callback {

    private List<BodyWeightItem> oldList;
    private List<BodyWeightItem> newList;

    public BodyWeightDiffUtilCallback(List<BodyWeightItem> oldList, List<BodyWeightItem> newList) {
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
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldList.get(oldItemPosition).weight == newList.get(newItemPosition).weight)
                && (oldList.get(oldItemPosition).date == newList.get(newItemPosition).date);
    }
}
