package com.corrot.room.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.corrot.room.R;
import com.corrot.room.activities.NewWorkoutActivity;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.viewmodel.RoutineViewModel;
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

        mRoutineViewModel.getAllRoutines().observe(this, routines -> routinesList = routines);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton floatingButton = view.findViewById(R.id.floating_button);
        // Start NewWorkoutActivity on floatingButton click
        floatingButton.setOnClickListener(v -> showExercisesDialog(getContext()).show());

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
                bundle.putSerializable("routine", routinesList.get(which - 1));
                newWorkoutIntent.putExtras(bundle);
                startActivity(newWorkoutIntent);
            }
        });
        return builder.create();
    }
}
