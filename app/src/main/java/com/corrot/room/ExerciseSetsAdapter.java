package com.corrot.room;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class ExerciseSetsAdapter extends RecyclerView.Adapter<ExerciseSetsAdapter.WorkoutViewHolder>  {

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView setTextView;
        private final EditText weightEditText;
        private final EditText repsEditText;

        private WorkoutViewHolder(View itemView) {
            super(itemView);
            setTextView = itemView.findViewById(R.id.new_set_text_view);
            weightEditText = itemView.findViewById(R.id.new_set_weight_edit_text);
            repsEditText = itemView.findViewById(R.id.new_set_reps_edit_text);
        }

        // spinner adapter
    }

    private final LayoutInflater mInflater;
    private List<ExerciseSetItem> mSets;

    ExerciseSetsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.new_exercise_item, viewGroup, false);
        return new ExerciseSetsAdapter.WorkoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder workoutViewHolder, int i) {
        if(mSets != null) {
            ExerciseSetItem set = mSets.get(i);
            set.no = workoutViewHolder.getAdapterPosition();
            workoutViewHolder.setTextView.setText(String.valueOf(set.no));
            workoutViewHolder.weightEditText.setText(String.valueOf(set.weight));
            workoutViewHolder.repsEditText.setText(String.valueOf(set.reps));
        }
    }

    @Override
    public int getItemCount() {
        if(mSets != null)
            return mSets.size();
        else return 0;
    }

    public void setSets(List<ExerciseSetItem> sets) {
        mSets = sets;
        notifyDataSetChanged();
    }

    public List<ExerciseSetItem> getSets() {
        return mSets;
    }
}
