package com.corrot.room;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corrot.room.adapters.DefinedWorkoutExercisesAdapter;
import com.corrot.room.db.entity.DefinedWorkout;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.utils.PreferencesManager;
import com.corrot.room.viewmodel.DefinedWorkoutViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewDefinedWorkoutDialog extends AppCompatDialogFragment {

    private EditText workoutNameEditText;
    private MaterialButton addExerciseButton;
    private RecyclerView recyclerView;
    private PreferencesManager pm;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_defined_workout, null);

        workoutNameEditText = view.findViewById(R.id.dialog_add_defined_workout_name);
        addExerciseButton = view.findViewById(R.id.dialog_add_defined_add_exercise);
        recyclerView = view.findViewById(R.id.dialog_add_defined_recycler_view);

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add item to recyclerView
            }
        });

        final DefinedWorkoutExercisesAdapter workoutListAdapter =
                new DefinedWorkoutExercisesAdapter(this.getContext());
        recyclerView.setAdapter(workoutListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        List<DefinedWorkoutExerciseItem> e = new ArrayList<>();
        DefinedWorkoutExerciseItem i = new DefinedWorkoutExerciseItem();
        i.name = "NAME"; i.sets = 5;
        e.add(i); e.add(i); e.add(i);
        workoutListAdapter.setExercises(e);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = workoutNameEditText.getText().toString();
                        List<String> exercises = new ArrayList<>();
                        // TODO: Get exercises from all recyclerView views
                        // TODO: handle exceptions
                        DefinedWorkout definedWorkout = new DefinedWorkout(name, exercises);
                        DefinedWorkoutViewModel definedWorkoutViewModel =
                                ViewModelProviders.of(getActivity()).get(DefinedWorkoutViewModel.class);
                        definedWorkoutViewModel.insertSingleWorkout(definedWorkout);
                        Toast.makeText(getContext(),
                                "Workout added",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
