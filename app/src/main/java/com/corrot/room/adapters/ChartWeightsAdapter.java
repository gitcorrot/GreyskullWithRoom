package com.corrot.room.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.corrot.room.ChartItem;
import com.corrot.room.R;
import com.corrot.room.utils.ChartWeightsDiffUtilCallback;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class ChartWeightsAdapter extends RecyclerView.Adapter<ChartWeightsAdapter.ChartWeightViewHolder> {

    class ChartWeightViewHolder extends RecyclerView.ViewHolder {
        private final TextView weightTextView;
        private final TextView dateTextView;
        private final TextView labelTextView;

        private ChartWeightViewHolder(View itemView) {
            super(itemView);
            weightTextView = itemView.findViewById(R.id.new_chart_workout_weight);
            dateTextView = itemView.findViewById(R.id.new_chart_workout_date);
            labelTextView = itemView.findViewById(R.id.new_chart_workout_label);
        }
    }

    private final LayoutInflater mInflater;
    private List<ChartItem> mChartItems;
    private String weightUnit;

    public ChartWeightsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        weightUnit = PreferencesManager.getInstance().getUnitSystem();
    }

    @NonNull
    @Override
    public ChartWeightViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(
                R.layout.recyclerview_chart_workout_item, viewGroup, false);
        return new ChartWeightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChartWeightViewHolder viewHolder, int i) {
        if (mChartItems != null) {
            String weight = mChartItems.get(i).weight + weightUnit;
            String date = MyTimeUtils.parseDate(mChartItems.get(i).date, MyTimeUtils.MAIN_FORMAT);
            String label = mChartItems.get(i).label;

            viewHolder.weightTextView.setText(weight);
            viewHolder.dateTextView.setText(date);
            viewHolder.labelTextView.setText(label);
        }
    }

    public void setChartItems(List<ChartItem> newChartItems) {
        if (this.mChartItems == null) {
            this.mChartItems = new ArrayList<>();
        }
        if (newChartItems != null) {
            ChartWeightsDiffUtilCallback callback =
                    new ChartWeightsDiffUtilCallback(this.mChartItems, newChartItems);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

            this.mChartItems.clear();
            this.mChartItems.addAll(newChartItems);
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return mChartItems != null ? mChartItems.size() : 0;
    }
}


