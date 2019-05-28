package com.corrot.room.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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

public class NewRoutineDialog extends DialogFragment {

    private EditText workoutNameEditText;
    private NewRoutineViewModel mNewRoutineViewModel;
    private RoutineViewModel mRoutineViewModel;
    private RoutineExercisesAdapter routineExercisesAdapter;

    private String mTag;
    private int mWorkoutId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mTag = getTag();
        String mWorkoutLabel;

        View view = View.inflate(getContext(), R.layout.dialog_fragment_add_routine, null);
        workoutNameEditText = view.findViewById(R.id.dialog_fragment_add_routine_workout_name);
        MaterialButton addExerciseButton = view.findViewById(R.id.dialog_fragment_add_routine_add_exercise);
        MaterialButton addButton = view.findViewById(R.id.dialog_fragment_add_button);
        MaterialButton cancelButton = view.findViewById(R.id.dialog_fragment_cancel_button);
        RecyclerView recyclerView = view.findViewById(R.id.dialog_fragment_add_routine_recycler_view);

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

        routineExercisesAdapter = new RoutineExercisesAdapter(getActivity());
        recyclerView.setAdapter(routineExercisesAdapter);
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
                        routineExercisesAdapter.getExerciseAt(viewHolder.getAdapterPosition());
                mNewRoutineViewModel.deleteExercise(e);
            }
        }).attachToRecyclerView(recyclerView);

        // TODO: Pass it when fragment opens this dialog.
        mNewRoutineViewModel.getAllExerciseItems().observe(this, routineExercisesAdapter::setExercises);

        addExerciseButton.setOnClickListener(v ->
                mNewRoutineViewModel.addExercise(new RoutineExerciseItem()));

        addButton.setOnClickListener(v -> {
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

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
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
