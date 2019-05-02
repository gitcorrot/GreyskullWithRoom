package com.corrot.room.adapters;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.corrot.room.R;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.utils.WorkoutsDiffUtilCallback;
import com.corrot.room.viewmodel.ExerciseViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsListAdapter extends RecyclerView.Adapter<WorkoutsListAdapter.WorkoutViewHolder> {

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutLabelTextView;
        private final TextView workoutDateTextView;
        private TextView setsTextView;
        private ImageButton moreButton;

        private WorkoutViewHolder(View view) {
            super(view);
            workoutLabelTextView = view.findViewById(R.id.recyclerview_workout_item_label);
            workoutDateTextView = view.findViewById(R.id.recyclerview_workout_item_date);
            setsTextView = view.findViewById(R.id.recyclerview_workout_sets);
            moreButton = view.findViewById(R.id.recyclerview_workout_options_button);
        }
    }

    private final LayoutInflater mInflater;
    private List<Workout> mWorkouts;
    private ExerciseViewModel mExerciseViewModel;
    private WorkoutsListAdapterInterface listener;

    public interface WorkoutsListAdapterInterface {
        void onEditClick(Workout workout);

        void onDeleteClick(Workout workout);

        void onShareClick(Workout workout);
    }

    public WorkoutsListAdapter(Context context, FragmentActivity activity,
                               WorkoutsListAdapterInterface listener) {
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
        this.mExerciseViewModel = ViewModelProviders.of(activity).get(ExerciseViewModel.class);
    }

    @NonNull
    @Override
    public WorkoutsListAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recyclerview_workout_item, viewGroup, false);
        final WorkoutViewHolder vh = new WorkoutViewHolder(view);

        View.OnClickListener editListener = v ->
                listener.onEditClick((mWorkouts.get(vh.getAdapterPosition())));
        vh.setsTextView.setOnClickListener(editListener);
        vh.workoutDateTextView.setOnClickListener(editListener);
        vh.workoutLabelTextView.setOnClickListener(editListener);

        vh.moreButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), vh.moreButton);
            popupMenu.getMenuInflater().inflate(R.menu.workout_menu, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.workout_menu_edit: {
                        listener.onEditClick(mWorkouts.get(vh.getAdapterPosition()));
                        return true;
                    }
                    case R.id.workout_menu_delete: {
                        listener.onDeleteClick(mWorkouts.get(vh.getAdapterPosition()));
                        return true;
                    }
                    case R.id.workout_menu_share: {
                        listener.onShareClick(mWorkouts.get(vh.getAdapterPosition()));
                        return true;
                    }
                }
                return false;
            });
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkoutsListAdapter.WorkoutViewHolder viewHolder, int i) {
        if (mWorkouts != null) {
            Workout w = mWorkouts.get(i);

            String date = MyTimeUtils.parseDate(w.workoutDate, MyTimeUtils.MAIN_FORMAT);
            viewHolder.workoutLabelTextView.setText(w.label);
            viewHolder.workoutDateTextView.setText(date);

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
                viewHolder.setsTextView
                        .setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
            });
        } else {
            viewHolder.workoutLabelTextView.setText("Workout label");
            viewHolder.workoutDateTextView.setText("Date");
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

    @Override
    public int getItemCount() {
        if (mWorkouts != null)
            return mWorkouts.size();
        else return 0;
    }
}
