package com.corrot.room.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corrot.room.DefinedWorkoutExerciseItem;
import com.corrot.room.NewDefinedWorkoutDialog;
import com.corrot.room.R;
import com.corrot.room.db.entity.DefinedWorkout;
import com.corrot.room.utils.DefinedWorkoutsDiffUtilCallback;
import com.corrot.room.viewmodel.DefinedWorkoutViewModel;
import com.corrot.room.viewmodel.NewDefinedWorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
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
    private DefinedWorkoutViewModel definedWorkoutViewModel;
    private FragmentActivity mActivity;

    public DefinedWorkoutsAdapter(Context context, FragmentActivity activity) {
        mInflater = LayoutInflater.from(context);
        mActivity = activity;
        definedWorkoutViewModel = ViewModelProviders.of(mActivity).get(DefinedWorkoutViewModel.class);
    }

    @NonNull
    @Override
    public DefinedWorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_defined_workout_item, viewGroup, false);
        final DefinedWorkoutViewHolder viewHolder = new DefinedWorkoutViewHolder(itemView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DefinedWorkout workout = mDefinedWorkouts.get(viewHolder.getAdapterPosition());
                workout.expanded = !workout.expanded;
                notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DefinedWorkout workout = mDefinedWorkouts.get(viewHolder.getAdapterPosition());
                showDeleteDialog(mActivity, workout).show();
            }
        });

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DefinedWorkout workout = mDefinedWorkouts.get(viewHolder.getAdapterPosition());
                List<DefinedWorkoutExerciseItem> exerciseItems = getWorkoutExercises(workout);

                NewDefinedWorkoutViewModel mNewDefinedWorkoutViewModel =
                        ViewModelProviders.of(mActivity).get(NewDefinedWorkoutViewModel.class);
                mNewDefinedWorkoutViewModel.init();
                mNewDefinedWorkoutViewModel.setExercises(exerciseItems);

                NewDefinedWorkoutDialog dialog = new NewDefinedWorkoutDialog();
                Bundle args = new Bundle();
                args.putInt("id", workout.id);
                args.putString("label", workout.label);
                dialog.setArguments(args);
                dialog.show(mActivity.getSupportFragmentManager(), "Edit");
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DefinedWorkoutViewHolder viewHolder, int i) {
        if (mDefinedWorkouts != null) {
            final DefinedWorkout workout = mDefinedWorkouts.get(i);
            viewHolder.labelTextView.setText(workout.label);

            StringBuilder exercises = new StringBuilder();
            for (String s : workout.exercises) {
                exercises.append(s);
                exercises.append("\n");
            }
            viewHolder.exercisesTextView.setText(exercises.toString());

            viewHolder.subItem.setVisibility(workout.expanded ? View.VISIBLE : View.GONE);


        }
    }

    public void setDefinedWorkouts(List<DefinedWorkout> newWorkouts) {
        if (this.mDefinedWorkouts == null) {
            this.mDefinedWorkouts = new ArrayList<>();
        }
        if (newWorkouts != null) {
            DefinedWorkoutsDiffUtilCallback callback =
                    new DefinedWorkoutsDiffUtilCallback(this.mDefinedWorkouts, newWorkouts);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
            this.mDefinedWorkouts.clear();
            this.mDefinedWorkouts.addAll(newWorkouts);
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        if (mDefinedWorkouts != null)
            return mDefinedWorkouts.size();
        else return 0;
    }

    private AlertDialog showDeleteDialog(FragmentActivity fragmentActivity, final DefinedWorkout workout) {
        return new AlertDialog.Builder(fragmentActivity.getThemedContext())
                .setTitle("Are you sure you want delete this workout?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        definedWorkoutViewModel.deleteWorkout(workout);
                    }
                })
                .create();
    }

    private List<DefinedWorkoutExerciseItem> getWorkoutExercises(DefinedWorkout workout) {
        List<DefinedWorkoutExerciseItem> exerciseItems = new ArrayList<>();

        for (int i = 0; i < workout.exercises.size(); i++) {
            // Parse for example "Squats = 2 sets." into object
            DefinedWorkoutExerciseItem item = new DefinedWorkoutExerciseItem();
            try {
                String[] name = workout.exercises.get(i).split(" - ");
                item.name = name[0];
                try {
                    String[] sets = name[1].split(" ");
                    item.sets = Integer.parseInt(sets[0]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e("DefinedWorkoutAdapter", "Can't find ' ' in String!");
                    item.sets = 0;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("DefinedWorkoutAdapter", "Can't find ' - ' in String!");
                item.name = "Name";
                item.sets = 0;
            }
            item.position = i;
            exerciseItems.add(item);
        }
        return exerciseItems;
    }
}
