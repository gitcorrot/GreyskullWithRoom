package com.corrot.room;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutIdTextView;
        private final TextView workoutDateTextView;

        private WorkoutViewHolder(View itemView) {
            super(itemView);
            workoutIdTextView = itemView.findViewById(R.id.recyclerview_workout_item_id);
            workoutDateTextView = itemView.findViewById(R.id.recyclerview_workout_item_date);
        }
    }

    private final LayoutInflater mInflater;
    private List<Workout> mWorkouts;

    WorkoutListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WorkoutListAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_workout_item, viewGroup, false);
        return new WorkoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutListAdapter.WorkoutViewHolder workoutViewHolder, int i) {
        if(mWorkouts != null) {
            Workout w = mWorkouts.get(i);
            workoutViewHolder.workoutIdTextView.setText("Workout ID: " + w.id);
            workoutViewHolder.workoutDateTextView.setText("Date: " + w.workoutDate.toString());
        }
        else {
            workoutViewHolder.workoutIdTextView.setText("Workout ID");
            workoutViewHolder.workoutDateTextView.setText("Date");
        }
    }

    public void setWorkouts(List<Workout> workouts) {
        mWorkouts = workouts;
        notifyDataSetChanged();
    }

    public Workout getWorkoutAt(int position) {
        return mWorkouts.get(position);
    }

    @Override
    public int getItemCount() {
        if(mWorkouts != null)
            return mWorkouts.size();
        else return 0;
    }
}
