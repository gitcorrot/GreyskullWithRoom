package com.corrot.room.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.corrot.room.R;
import com.corrot.room.adapters.ExercisesListViewAdapter;
import com.corrot.room.utils.PreferencesManager;

public class NewExerciseNameDialog extends AppCompatDialogFragment {

    private EditText exerciseEditText;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = View.inflate(getContext(), R.layout.dialog_new_exercise_name, null);

        PreferencesManager pm = PreferencesManager.getInstance();

        exerciseEditText = view.findViewById(R.id.dialog_new_exercise_edit_text);
        ListView exercisesListView = view.findViewById(R.id.dialog_new_exercise_list_view);
        Button addExerciseButton = view.findViewById(R.id.dialog_new_exercise_add_button);

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

        builder.setView(view)
                .setTitle("Exercises management")
                .setPositiveButton("Apply", null)
                .setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss());

        final AlertDialog dialog = builder.create();

        // This code is needed to override positive button listener to don't close dialog.
        dialog.setOnShowListener(d -> {
            Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(v -> {
                // apply new exercises list to pm
                pm.saveExercises(listViewAdapter.getList());
                dismiss();
            });
        });
        return dialog;
    }
}
