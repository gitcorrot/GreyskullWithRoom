package com.corrot.room.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corrot.room.DefinedWorkout;
import com.corrot.room.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DefinedWorkoutsAdapter extends RecyclerView.Adapter<DefinedWorkoutsAdapter.DefinedWorkoutViewHolder> {

    class DefinedWorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView labelTextView;
        private final TextView exercisesTextView;
        private final LinearLayout subItem;
        private final ImageButton deleteButton;
        private final ImageButton editButton;

        private DefinedWorkoutViewHolder(View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.defined_workout_label);
            exercisesTextView = itemView.findViewById(R.id.defined_workout_exercises);
            subItem = itemView.findViewById(R.id.defined_workout_sub_item);
            deleteButton = itemView.findViewById(R.id.defined_workout_delete_button);
            editButton = itemView.findViewById(R.id.defined_workout_edit_button);
        }
    }

    private final LayoutInflater mInflater;
    private List<DefinedWorkout> mDefinedWorkouts;
    //private FragmentActivity mActivity;

    public DefinedWorkoutsAdapter(Context context/*, FragmentActivity activity*/) {
        mInflater = LayoutInflater.from(context);
       // mActivity = activity;
    }

    @NonNull
    @Override
    public DefinedWorkoutsAdapter.DefinedWorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_defined_workout_item, viewGroup, false);
        return new DefinedWorkoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DefinedWorkoutViewHolder viewHolder, int i) {
        if (mDefinedWorkouts != null) {
            final DefinedWorkout workout = mDefinedWorkouts.get(i);
            viewHolder.labelTextView.setText(workout.label);

            StringBuilder exercises = new StringBuilder();
            for(String s : workout.exercises) {
                exercises.append(s);
                exercises.append("\n");
            }
            viewHolder.exercisesTextView.setText(exercises.toString());

            viewHolder.subItem.setVisibility(workout.expanded ? View.VISIBLE : View.GONE);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workout.expanded = !workout.expanded;
                    notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });

            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDefinedWorkouts.remove(workout);
                    notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            });

            viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open edit fragment
                }
            });
        }
    }

    public void setDefinedWorkouts(List<DefinedWorkout> newWorkouts) {
        if (this.mDefinedWorkouts == null) {
            this.mDefinedWorkouts = new ArrayList<>();
        }
        if (newWorkouts != null) {
            this.mDefinedWorkouts.clear();
            this.mDefinedWorkouts.addAll(newWorkouts);
        }
    }

    @Override
    public int getItemCount() {
        if (mDefinedWorkouts != null)
            return mDefinedWorkouts.size();
        else return 0;
    }
}
