package com.corrot.room.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.corrot.room.R;
import com.corrot.room.activities.NewWorkoutActivity;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.utils.WorkoutsDiffUtilCallback;
import com.corrot.room.viewmodel.ExerciseViewModel;
import com.corrot.room.viewmodel.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsListAdapter extends RecyclerView.Adapter<WorkoutsListAdapter.WorkoutViewHolder> {

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutIdTextView;
        private final TextView workoutDateTextView;
        private TextView setsTextView;
        private ImageButton moreButton;

        private WorkoutViewHolder(View view) {
            super(view);
            workoutIdTextView = view.findViewById(R.id.recyclerview_workout_item_label);
            workoutDateTextView = view.findViewById(R.id.recyclerview_workout_item_date);
            setsTextView = view.findViewById(R.id.recyclerview_workout_sets);
            moreButton = view.findViewById(R.id.recyclerview_workout_options_button);
        }
    }

    private final LayoutInflater mInflater;
    private List<Workout> mWorkouts;
    private ExerciseViewModel mExerciseViewModel;
    private WorkoutViewModel mWorkoutViewModel;
    private FragmentActivity mActivity;

    public WorkoutsListAdapter(Context context, FragmentActivity activity) {
        mInflater = LayoutInflater.from(context);
        mActivity = activity;

        mExerciseViewModel = ViewModelProviders.of(activity).get(ExerciseViewModel.class);
        mWorkoutViewModel = ViewModelProviders.of(activity).get(WorkoutViewModel.class);
    }

    @NonNull
    @Override
    public WorkoutsListAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recyclerview_workout_item, viewGroup, false);
        final WorkoutViewHolder vh = new WorkoutViewHolder(view);

        vh.moreButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(mActivity, vh.moreButton);
            popupMenu.getMenuInflater().inflate(R.menu.workout_menu, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.workout_menu_edit: {
                        Intent updateWorkoutIntent = new Intent(mActivity, NewWorkoutActivity.class);
                        updateWorkoutIntent.putExtra("flags", NewWorkoutActivity.FLAG_UPDATE_WORKOUT);
                        updateWorkoutIntent.putExtra("workoutId", mWorkouts.get(vh.getAdapterPosition()).id);
                        mActivity.startActivity(updateWorkoutIntent);
                        return true;
                    }
                    case R.id.workout_menu_delete: {
                        Workout w = getWorkoutAt(vh.getAdapterPosition());
                        mWorkoutViewModel.deleteWorkout(w);
                        Toast.makeText(view.getContext(),
                                "Workout deleted!", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.workout_menu_share: {
                        // TODO: Share workout.
                        return true;
                    }
                }
                return false;
            });
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkoutsListAdapter.WorkoutViewHolder workoutViewHolder, int i) {
        if (mWorkouts != null) {
            Workout w = mWorkouts.get(i);
            workoutViewHolder.workoutIdTextView.setText(w.label);
            String date = MyTimeUtils.parseDate(w.workoutDate, MyTimeUtils.MAIN_FORMAT);
            workoutViewHolder.workoutDateTextView.setText(date);

            // exercises and sets
            mExerciseViewModel.getExercisesByWorkoutId(w.id, exercises -> {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                StringBuilder rxwBuilder = new StringBuilder();
                for (Exercise e : exercises) {
                    if (spannableStringBuilder.length() > 1) {
                        spannableStringBuilder.append("\n");
                    }
                    spannableStringBuilder
                            .append(e.name, new StyleSpan(Typeface.BOLD),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            .append("\n");

                    if (e.weights.size() == e.reps.size()) {
                        rxwBuilder.setLength(0);
                        for (int j = 0; j < e.weights.size(); j++) {
                            rxwBuilder
                                    .append(e.reps.get(j))
                                    .append("x")
                                    .append(e.weights.get(j));
                            if (j < e.weights.size() - 1) rxwBuilder.append("kg, ");
                            else rxwBuilder.append("kg");
                        }
                    }
                    spannableStringBuilder.append(rxwBuilder.toString());
                }
                workoutViewHolder.setsTextView
                        .setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
            });

        } else {
            workoutViewHolder.workoutIdTextView.setText("Workout label");
            workoutViewHolder.workoutDateTextView.setText("Date");
        }
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
