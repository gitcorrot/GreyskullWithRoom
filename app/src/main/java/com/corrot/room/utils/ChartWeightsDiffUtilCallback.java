package com.corrot.room.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.corrot.room.ChartItem;

import java.util.List;

public class ChartWeightsDiffUtilCallback extends DiffUtil.Callback {

    private List<ChartItem> oldList;
    private List<ChartItem> newList;

    public ChartWeightsDiffUtilCallback(List<ChartItem> oldList, List<ChartItem> newList) {
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
                && (oldList.get(oldItemPosition).date == newList.get(newItemPosition).date)
                && (oldList.get(oldItemPosition).label.equals(newList.get(newItemPosition).label));
    }
}
