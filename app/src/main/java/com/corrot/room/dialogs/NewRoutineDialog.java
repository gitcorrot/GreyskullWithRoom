package com.corrot.room.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corrot.room.R;
import com.corrot.room.RoutineExerciseItem;
import com.corrot.room.adapters.RoutineExercisesAdapter;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.viewmodel.NewRoutineViewModel;
import com.corrot.room.viewmodel.RoutineViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewRoutineDialog extends AppCompatDialogFragment {

    private EditText workoutNameEditText;
    private NewRoutineViewModel mNewRoutineViewModel;
    private RoutineViewModel mRoutineViewModel;

    private String mTag;
    private int mWorkoutId;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        mTag = getTag();
        String mWorkoutLabel;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(),
                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        View view = View.inflate(getContext(), R.layout.dialog_add_routine, null);
        workoutNameEditText = view.findViewById(R.id.dialog_add_routine_workout_name);
        MaterialButton addExerciseButton = view.findViewById(R.id.dialog_add_routine_add_exercise);
        RecyclerView recyclerView = view.findViewById(R.id.dialog_add_routine_recycler_view);

        if (mTag != null && mTag.equals("Edit")) {
            Bundle args = getArguments();
            if (args != null) {
                mWorkoutId = args.getInt("id", 0);
                mWorkoutLabel = args.getString("label");
                if (mWorkoutId == 0) {
                    Log.e("NewRoutineDialog", "Can't find workout ID!");
                }
                if (mWorkoutLabel != null && mWorkoutLabel.equals("")) {
                    Log.e("NewRoutineDialog", "Can't find workout label!");
                } else if (mWorkoutLabel != null) {
                    workoutNameEditText.setText(mWorkoutLabel);
                }
            }
        }

        mRoutineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);
        mNewRoutineViewModel = ViewModelProviders.of(this).get(NewRoutineViewModel.class);
        mNewRoutineViewModel.init();

        final RoutineExercisesAdapter workoutListAdapter = new RoutineExercisesAdapter(getActivity());
        recyclerView.setAdapter(workoutListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                RoutineExerciseItem e =
                        workoutListAdapter.getExerciseAt(viewHolder.getAdapterPosition());
                mNewRoutineViewModel.deleteExercise(e);
            }
        }).attachToRecyclerView(recyclerView);

        // TODO: Pass it when fragment opens this dialog.
        mNewRoutineViewModel.getAllExerciseItems().observe(this, workoutListAdapter::setExercises);

        addExerciseButton.setOnClickListener(v ->
                mNewRoutineViewModel.addExercise(new RoutineExerciseItem()));

        builder.setView(view)
                .setPositiveButton("Add", null)
                .setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss());

        final AlertDialog dialog = builder.create();

        // This code is needed to override positive button listener to don't close dialog.
        dialog.setOnShowListener(d -> {
            Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(v -> {
                String name = workoutNameEditText.getText().toString();
                if (name.isEmpty()) {
                    workoutNameEditText.requestFocus();
                    workoutNameEditText.setError("Please add routine name!");
                } else {
                    try {
                        Routine routine = getRoutineFromViewModel(name);

                        switch (mTag) {
                            case "Add":
                                if (!isRoutineInDatabase(routine.label)) {
                                    mRoutineViewModel.insertSingleRoutine(routine);
                                    Toast.makeText(getContext(),
                                            "Routine added",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                } else {
                                    workoutNameEditText.requestFocus();
                                    workoutNameEditText.setError("Routine with this name exists!");
                                    return;
                                }
                            case "Edit":
                                routine.id = mWorkoutId;
                                mRoutineViewModel.updateRoutine(routine);
                                Toast.makeText(getContext(),
                                        "Routine updated",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                        dismiss();
                    } catch (RuntimeException e) {
                        Toast.makeText(getContext(), "Set exercise name first!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        return dialog;
    }

    private boolean isRoutineInDatabase(String name) {
        boolean is = true;
        try {
            if (mRoutineViewModel.getRoutineByName(name) == null) {
                is = false;
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e("NewRoutineDialog", e.getLocalizedMessage());
        }
        return is;
    }

    private Routine getRoutineFromViewModel(String name) throws RuntimeException {
        List<String> exercises = new ArrayList<>();
        List<Integer> sets = new ArrayList<>();
        List<RoutineExerciseItem> items = mNewRoutineViewModel.getAllExerciseItems().getValue();
        if (items != null) {
            for (RoutineExerciseItem i : items) {
                if (i.name.isEmpty()) {
                    throw new RuntimeException("Name empty!");
                }
                exercises.add(i.name);
                sets.add(i.sets);
            }
        }
        return new Routine(name, exercises, sets);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNewRoutineViewModel.destroyInstance();
    }
}
