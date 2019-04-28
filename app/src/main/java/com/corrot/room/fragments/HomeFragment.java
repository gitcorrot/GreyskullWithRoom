package com.corrot.room.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.corrot.room.R;
import com.corrot.room.activities.NewWorkoutActivity;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.viewmodel.RoutineViewModel;
import com.corrot.room.viewmodel.WorkoutViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Routine> routinesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        routinesList = new ArrayList<>();
        RoutineViewModel mRoutineViewModel =
                ViewModelProviders.of(this).get(RoutineViewModel.class);

        WorkoutViewModel mWorkoutViewModel =
                ViewModelProviders.of(this).get(WorkoutViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton floatingButton = view.findViewById(R.id.floating_button);
        TextView lastWorkoutTextView = view.findViewById(R.id.fragment_home_last_workout_text_view);
        TextView workoutsCountTextVIew = view.findViewById(R.id.fragment_home_workouts_count_text_view);

        mRoutineViewModel.getAllRoutines().observe(this, routines -> routinesList = routines);
        mWorkoutViewModel.getLastWorkout().observe(this, lastWorkout -> {
            if (lastWorkout != null) {
                String date = MyTimeUtils.parseDate(lastWorkout.workoutDate, MyTimeUtils.MAIN_FORMAT);
                String sb = "Your last workout: " + date;
                lastWorkoutTextView.setText(sb);
            } else {
                String sb = "Add your first workout!";
                lastWorkoutTextView.setText(sb);
            }
        });
        mWorkoutViewModel.getTotalCount().observe(this, count -> {
            String sb;
            if (count != null) sb = "Total number of workouts: " + count;
            else sb = "Total number of workouts: 0";
            workoutsCountTextVIew.setText(sb);
        });


        // Start NewWorkoutActivity on floatingButton click
        floatingButton.setOnClickListener(v -> {
            if (!getActivity().isFinishing()) {
                showExercisesDialog(getContext()).show();
            }
        });

        return view;
    }

    private AlertDialog showExercisesDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose workout");

        List<String> routines = new ArrayList<>();
        routines.add("Normal workout");
        if (routinesList != null) {
            for (Routine r : routinesList) {
                routines.add(r.label);
            }
        }

        String[] routinesArray = new String[routines.size()];
        routines.toArray(routinesArray);

        builder.setItems(routinesArray, (dialog, which) -> {
            if (routinesArray.length >= which) {
                Intent newWorkoutIntent = new Intent(getContext(), NewWorkoutActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flags", NewWorkoutActivity.FLAG_ADD_WORKOUT);
                // 0 is "Normal workout"
                if (which > 0) {
                    bundle.putSerializable("routine", routinesList.get(which - 1));
                }
                newWorkoutIntent.putExtras(bundle);
                startActivity(newWorkoutIntent);
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
