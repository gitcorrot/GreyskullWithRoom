package com.corrot.room.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.corrot.room.R;
import com.corrot.room.RoutineExerciseItem;
import com.corrot.room.adapters.RoutineExercisesAdapter;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.viewmodel.NewRoutineViewModel;
import com.corrot.room.viewmodel.RoutineViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewRoutineDialog extends AppCompatDialogFragment {

    private EditText workoutNameEditText;
    private NewRoutineViewModel mNewRoutineViewModel;

    private String mTag;
    private int mWorkoutId;
    private FragmentActivity mActivity;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        mActivity = getActivity();
        mTag = getTag();
        String mWorkoutLabel;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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

        mNewRoutineViewModel = ViewModelProviders.of(this).get(NewRoutineViewModel.class);
        mNewRoutineViewModel.init();

        final RoutineExercisesAdapter workoutListAdapter = new RoutineExercisesAdapter(mActivity);
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
        mNewRoutineViewModel.getAllExerciseItems().observe(mActivity, routineExerciseItems ->
                workoutListAdapter.setExercises(routineExerciseItems));

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
                    Routine routine = getRoutineFromViewModel(name);
                    RoutineViewModel routineViewModel =
                            ViewModelProviders.of(mActivity).get(RoutineViewModel.class);
                    switch (mTag) {
                        case "Add":
                            routineViewModel.insertSingleRoutine(routine);
                            Toast.makeText(getContext(),
                                    "Workout added",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case "Edit":
                            routine.id = mWorkoutId;
                            routineViewModel.updateRoutine(routine);
                            Toast.makeText(getContext(),
                                    "Workout updated",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                    dismiss();
                }
            });
        });
        return dialog;
    }

    private Routine getRoutineFromViewModel(String name) {
        List<String> exercises = new ArrayList<>();
        // TODO: EXCEPTIONS
        List<RoutineExerciseItem> items = mNewRoutineViewModel.getAllExerciseItems().getValue();
        if (items != null) {
            for (RoutineExerciseItem i : items) {
                String s = i.name + " - " + i.sets + " sets.";
                exercises.add(s);
            }
        }
        return new Routine(name, exercises);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNewRoutineViewModel.destroyInstance();
    }
}
