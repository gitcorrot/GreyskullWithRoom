package com.corrot.room.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.corrot.room.R;
import com.corrot.room.adapters.ExercisesListViewAdapter;
import com.corrot.room.utils.PreferencesManager;

public class ExercisesManagementDialog extends DialogFragment {

    private EditText exerciseEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getContext(), R.layout.dialog_exercises_management, null);
        PreferencesManager pm = PreferencesManager.getInstance();

        exerciseEditText = view.findViewById(R.id.dialog_exercises_edit_text);
        ListView exercisesListView = view.findViewById(R.id.dialog_exercises_list_view);
        Button addExerciseButton = view.findViewById(R.id.dialog_exercises_add_exercise_button);
        Button saveButton = view.findViewById(R.id.dialog_exercises_save_button);
        Button cancelButton = view.findViewById(R.id.dialog_exercises_cancel_button);

        ExercisesListViewAdapter listViewAdapter = new ExercisesListViewAdapter(pm.getExercisesList(), getContext());
        exercisesListView.setAdapter(listViewAdapter);

        addExerciseButton.setOnClickListener(v -> {
            String exercise = exerciseEditText.getText().toString();
            if (exercise.isEmpty()) {
                exerciseEditText.requestFocus();
                exerciseEditText.setError("Please put exercise name first!");

            } else {
                listViewAdapter.addExercise(exercise);
                Toast.makeText(getContext(),
                        "Exercise added",
                        Toast.LENGTH_SHORT).show();
                exerciseEditText.setText(null);
            }
        });

        saveButton.setOnClickListener(v -> {
            // apply new exercises list to pm
            pm.saveExercises(listViewAdapter.getList());
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }
}
