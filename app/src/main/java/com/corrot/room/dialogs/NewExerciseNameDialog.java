package com.corrot.room.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.corrot.room.R;
import com.corrot.room.utils.PreferencesManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NewExerciseNameDialog extends AppCompatDialogFragment {

    private EditText exerciseEditText;
    private PreferencesManager pm;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = View.inflate(getContext(), R.layout.dialog_new_exercise_name, null);
        exerciseEditText = view.findViewById(R.id.dialog_new_exercise_edit_text);
        pm = PreferencesManager.getInstance();

        builder.setView(view)
                .setTitle("Add new exercise")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss());

        final AlertDialog dialog = builder.create();

        // This code is needed to override positive button listener to don't close dialog.
        dialog.setOnShowListener(d-> {
            Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(v -> {
                String exercise = exerciseEditText.getText().toString();
                if (exercise.isEmpty()) {
                    exerciseEditText.requestFocus();
                    exerciseEditText.setError("Please put exercise name first!");

                } else {
                    pm.addExercise(exercise);
                    Toast.makeText(getContext(),
                            "Exercise added",
                            Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        });
        return dialog;
    }
}
