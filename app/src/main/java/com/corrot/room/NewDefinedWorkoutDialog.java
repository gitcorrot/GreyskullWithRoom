package com.corrot.room;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.corrot.room.adapters.DefinedWorkoutExercisesAdapter;
import com.corrot.room.db.entity.DefinedWorkout;
import com.corrot.room.viewmodel.DefinedWorkoutViewModel;
import com.corrot.room.viewmodel.NewDefinedWorkoutViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewDefinedWorkoutDialog extends AppCompatDialogFragment {

    private EditText workoutNameEditText;
    private MaterialButton addExerciseButton;
    private RecyclerView recyclerView;
    private NewDefinedWorkoutViewModel mNewDefinedWorkoutViewModel;

    private String mTag;
    private String mWorkoutLabel;
    private int mWorkoutId;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_defined_workout, null);

        workoutNameEditText = view.findViewById(R.id.dialog_add_defined_workout_name);
        addExerciseButton = view.findViewById(R.id.dialog_add_defined_add_exercise);
        recyclerView = view.findViewById(R.id.dialog_add_defined_recycler_view);

        mTag = getTag();

        if (mTag != null && mTag.equals("Edit")) {
            Bundle args = getArguments();
            if (args != null) {
                mWorkoutId = args.getInt("id", 0);
                mWorkoutLabel = args.getString("label");
                if (mWorkoutId == 0) {
                    Log.e("NewDefinedWorkoutDialog", "Can't find workout ID!");
                }
                if (mWorkoutLabel != null && mWorkoutLabel.equals("")) {
                    Log.e("NewDefinedWorkoutDialog", "Can't find workout label!");
                } else if (mWorkoutLabel != null && !mWorkoutLabel.equals("")){
                    workoutNameEditText.setText(mWorkoutLabel);
                }
            }
        }

        mNewDefinedWorkoutViewModel =
                ViewModelProviders.of(this).get(NewDefinedWorkoutViewModel.class);
        mNewDefinedWorkoutViewModel.init(); // ?


        final DefinedWorkoutExercisesAdapter workoutListAdapter =
                new DefinedWorkoutExercisesAdapter(getActivity());
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
                DefinedWorkoutExerciseItem e =
                        workoutListAdapter.getExerciseAt(viewHolder.getAdapterPosition());
                mNewDefinedWorkoutViewModel.deleteExercise(e);
            }
        }).attachToRecyclerView(recyclerView);

        mNewDefinedWorkoutViewModel.getAllExerciseItems().observe(getActivity(),
                new Observer<List<DefinedWorkoutExerciseItem>>() {
                    @Override
                    public void onChanged(List<DefinedWorkoutExerciseItem> definedWorkoutExerciseItems) {
                        workoutListAdapter.setExercises(definedWorkoutExerciseItems);
                    }
                });


        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewDefinedWorkoutViewModel.addExercise(new DefinedWorkoutExerciseItem());
            }
        });


        builder.setView(view)
                .setPositiveButton(mTag, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = workoutNameEditText.getText().toString();


                        List<String> exercises = new ArrayList<>();
                        List<DefinedWorkoutExerciseItem> items
                                = mNewDefinedWorkoutViewModel.getAllExerciseItems().getValue();
                        if (items != null) {
                            for (DefinedWorkoutExerciseItem i : items) {
                                String s = i.name + " - " + i.sets + " sets.";
                                exercises.add(s);
                            }
                        }
                        // TODO: handle exceptions
                        DefinedWorkout definedWorkout = new DefinedWorkout(name, exercises);
                        DefinedWorkoutViewModel definedWorkoutViewModel =
                                ViewModelProviders.of(getActivity()).get(DefinedWorkoutViewModel.class);

                        switch (mTag) {
                            case "Add":
                                definedWorkoutViewModel.insertSingleWorkout(definedWorkout);
                                Toast.makeText(getContext(),
                                        "Workout added",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case "Edit":
                                definedWorkout.id = mWorkoutId;
                                definedWorkoutViewModel.updateWorkout(definedWorkout);
                                Toast.makeText(getContext(),
                                        "Workout updated",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNewDefinedWorkoutViewModel.destroyInstance();
    }
}
