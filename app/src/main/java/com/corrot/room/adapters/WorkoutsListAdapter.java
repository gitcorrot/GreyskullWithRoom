package com.corrot.room.adapters;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corrot.room.R;
import com.corrot.room.activities.NewWorkoutActivity;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.utils.WorkoutsDiffUtilCallback;
import com.corrot.room.viewmodel.ExerciseViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkoutsListAdapter extends RecyclerView.Adapter<WorkoutsListAdapter.WorkoutViewHolder> {

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutIdTextView;
        private final TextView workoutDateTextView;
        private TextView setsTextView;

        private WorkoutViewHolder(View itemView) {
            super(itemView);
            workoutIdTextView = itemView.findViewById(R.id.recyclerview_workout_item_id);
            workoutDateTextView = itemView.findViewById(R.id.recyclerview_workout_item_date);
            setsTextView = itemView.findViewById(R.id.recyclerview_workout_sets);
        }
    }

    private final LayoutInflater mInflater;
    private List<Workout> mWorkouts;
    private ExerciseViewModel mExerciseViewModel;
    private FragmentActivity mActivity;

    public WorkoutsListAdapter(Context context, FragmentActivity activity) {
        mInflater = LayoutInflater.from(context);
        mActivity = activity;

        mExerciseViewModel = ViewModelProviders // ??
                .of((AppCompatActivity) context).get(ExerciseViewModel.class);

    }

    @NonNull
    @Override
    public WorkoutsListAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_workout_item, viewGroup, false);
        return new WorkoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkoutsListAdapter.WorkoutViewHolder workoutViewHolder, int i) {
        if (mWorkouts != null) {
            Workout w = mWorkouts.get(i);
            workoutViewHolder.workoutIdTextView.setText("Daily workout"/* ID: " + w.id*/);
            String date = MyTimeUtils.parseDate(w.workoutDate, MyTimeUtils.MAIN_FORMAT);
            workoutViewHolder.workoutDateTextView.setText(date);

            // exercises and sets
            try {

                List<Exercise> exercises = mExerciseViewModel.getExercisesByWorkoutId(w.id);

                String exercisesText = "";

                for (Exercise e : exercises) {

                    String rxw = "";

                    exercisesText += "\n" + e.name + "\n";

                    if (e.weights.size() == e.reps.size()) {
                        for (int j = 0; j < e.weights.size(); j++) {
                            rxw += e.reps.get(j) + "x" + e.weights.get(j) + "kg, ";
                        }
                    }

                    exercisesText += rxw;
                }
                workoutViewHolder.setsTextView.setText(exercisesText);

            } catch (InterruptedException ie) {
                Log.e("asdasd", ie.getMessage());
            } catch (ExecutionException ee) {
                Log.e("asdasd", ee.getMessage());
            }
        } else {
            workoutViewHolder.workoutIdTextView.setText("Workout ID");
            workoutViewHolder.workoutDateTextView.setText("Date");
        }

        // OPEN WORKOUT IN EDITOR
        workoutViewHolder.setsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateWorkoutIntent =
                        new Intent(mActivity, NewWorkoutActivity.class);
                updateWorkoutIntent.putExtra("flags", NewWorkoutActivity.FLAG_UPDATE_WORKOUT);
                updateWorkoutIntent.putExtra("workoutId",
                        mWorkouts.get(workoutViewHolder.getAdapterPosition()).id);
                mActivity.startActivity(updateWorkoutIntent);
            }
        });
    }

    public void setWorkouts(List<Workout> newWorkouts) {
        if (this.mWorkouts == null) {
            this.mWorkouts = new ArrayList<>();
        }
        if (newWorkouts != null) {
            WorkoutsDiffUtilCallback callback =
                    new WorkoutsDiffUtilCallback(this.mWorkouts, newWorkouts);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

            this.mWorkouts.clear();
            this.mWorkouts.addAll(newWorkouts);
            diffResult.dispatchUpdatesTo(this);
        }
    }

    public Workout getWorkoutAt(int position) {
        return mWorkouts.get(position);
    }

    @Override
    public int getItemCount() {
        if (mWorkouts != null)
            return mWorkouts.size();
        else return 0;
    }
}
