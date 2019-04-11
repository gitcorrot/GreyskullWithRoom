package com.corrot.room.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corrot.room.BodyWeightItem;
import com.corrot.room.R;
import com.corrot.room.utils.BodyWeightDiffUtilCallback;
import com.corrot.room.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BodyWeightsAdapter extends RecyclerView.Adapter<BodyWeightsAdapter.BodyWeightViewHolder> {

    class BodyWeightViewHolder extends RecyclerView.ViewHolder {
        private final TextView weightTextView;
        private final TextView dateTextView;
        private final TextView diffTextView;

        private BodyWeightViewHolder(View itemView) {
            super(itemView);
            weightTextView = itemView.findViewById(R.id.new_body_weight_weight);
            dateTextView = itemView.findViewById(R.id.new_body_weight_date);
            diffTextView = itemView.findViewById(R.id.new_body_weight_diff);
        }
    }

    private final LayoutInflater mInflater;
    private List<BodyWeightItem> mBodyWeights;

    public BodyWeightsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BodyWeightsAdapter.BodyWeightViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_body_weight_item, viewGroup, false);
        return new BodyWeightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BodyWeightsAdapter.BodyWeightViewHolder bodyWeightViewHolder, int i) {
        if (mBodyWeights != null) {
            String diff;
            String weight = String.valueOf(mBodyWeights.get(i).weight) + "kg";
            String date = MyTimeUtils.parseDate(mBodyWeights.get(i).date, MyTimeUtils.MAIN_FORMAT);

            if (i < mBodyWeights.size() - 1) {
                float lastWeight = mBodyWeights.get(i).weight;
                float currentWeight = mBodyWeights.get(i + 1).weight;
                float diffWeight = lastWeight - currentWeight;
                if (diffWeight <= 0)
                    diff = String.valueOf(lastWeight - currentWeight) + "kg";
                else
                    diff = "+" + String.valueOf(lastWeight - currentWeight) + "kg";

            } else {
                diff = "---";
            }

            bodyWeightViewHolder.weightTextView.setText(weight);
            bodyWeightViewHolder.dateTextView.setText(date);
            bodyWeightViewHolder.diffTextView.setText(diff);
        }
    }

    public void setBodyWeights(List<BodyWeightItem> newBodyWeights) {
        if (this.mBodyWeights == null) {
            this.mBodyWeights = new ArrayList<>();
        }
        if (newBodyWeights != null) {
            BodyWeightDiffUtilCallback callback =
                    new BodyWeightDiffUtilCallback(this.mBodyWeights, newBodyWeights);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

            this.mBodyWeights.clear();
            this.mBodyWeights.addAll(newBodyWeights);
            diffResult.dispatchUpdatesTo(this);
        }
    }

    public BodyWeightItem getBodyWeightItemAt(int position) {
        return mBodyWeights.get(position);
    }

    @Override
    public int getItemCount() {
        return mBodyWeights != null ? mBodyWeights.size() : 0;
    }
}


