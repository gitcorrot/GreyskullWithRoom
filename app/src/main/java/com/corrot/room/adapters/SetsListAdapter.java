package com.corrot.room.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.corrot.room.ExerciseSetItem;
import com.corrot.room.R;

import java.util.List;

public class SetsListAdapter extends RecyclerView.Adapter<SetsListAdapter.exerciseSetViewHolder>  {

    class exerciseSetViewHolder extends RecyclerView.ViewHolder {
        private final TextView setTextView;
        private final EditText weightEditText;
        private final EditText repsEditText;

        private exerciseSetViewHolder(View itemView) {
            super(itemView);
            setTextView = itemView.findViewById(R.id.new_set_text_view);
            weightEditText = itemView.findViewById(R.id.new_set_weight_edit_text);
            repsEditText = itemView.findViewById(R.id.new_set_reps_edit_text);
        }
    }

    private final LayoutInflater mInflater;
    private List<ExerciseSetItem> mSets;

    public SetsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public exerciseSetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_set_item, viewGroup, false);
        return new exerciseSetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull exerciseSetViewHolder workoutViewHolder, int i) {

        if(mSets!= null) {
            ExerciseSetItem item = mSets.get(i);
            workoutViewHolder.setTextView.setText(String.valueOf(i));
            workoutViewHolder.weightEditText.setText(String.valueOf(item.weight));
            workoutViewHolder.repsEditText.setText(String.valueOf(item.reps));
        }
    }

    @Override
    public int getItemCount() {
        if(mSets != null)
            return mSets.size();
        else {
            return 0;
        }
    }

    public void setSets(List<ExerciseSetItem> sets) {
        mSets = sets;
        notifyDataSetChanged();
    }

    public List<ExerciseSetItem> getSets() {
        return mSets;
    }
}
