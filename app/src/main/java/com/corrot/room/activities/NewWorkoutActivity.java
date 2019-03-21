package com.corrot.room.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.adapters.ExercisesListAdapter;
import com.corrot.room.R;
import com.corrot.room.viewmodel.NewWorkoutViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity {

    TextView dateTextView;
    RecyclerView exercisesRecyclerView;
    Button addExerciseButton;
    ImageButton saveButton;
    private NewWorkoutViewModel newWorkoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        dateTextView = findViewById(R.id.new_workout_date_text_view);
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        addExerciseButton = findViewById(R.id.new_exercise_button);
        saveButton = findViewById(R.id.new_workout_save_button);

        newWorkoutViewModel = ViewModelProviders.of(this).get(NewWorkoutViewModel.class);
        newWorkoutViewModel.init();

        // TODO: Do it right way
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
        dateTextView.setText(sdf.format(date));

        final ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(this,this);
        exercisesRecyclerView.setAdapter(exercisesListAdapter);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        newWorkoutViewModel.getAllExerciseItems().observe(this, new Observer<List<ExerciseItem>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseItem> exerciseItems) {
                // TODO: use DiffUtils
                exercisesListAdapter.setExercises(exerciseItems);
            }
        });

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWorkoutViewModel.addExercise(new ExerciseItem("New exercise"));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to DB.

                //debugging:
                newWorkoutViewModel.updateSet(new ExerciseSetItem(0,102.5f,5), 1);
            }
        });
    }
}
