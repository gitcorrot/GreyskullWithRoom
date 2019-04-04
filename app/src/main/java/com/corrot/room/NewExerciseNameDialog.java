package com.corrot.room;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.corrot.room.utils.PreferencesManager;

public class NewExerciseNameDialog extends AppCompatDialogFragment {

    private EditText exerciseEditText;
    private PreferencesManager pm;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_exercise_name, null);
        exerciseEditText = view.findViewById(R.id.dialog_new_exercise_edit_text);
        pm = PreferencesManager.getInstance();

        builder.setView(view)
                .setTitle("Add new exercise")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String exercise = exerciseEditText.getText().toString();
                        pm.addExercise(exercise);
                        Toast.makeText(getContext(),
                                "Exercise added",
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
