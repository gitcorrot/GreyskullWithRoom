package com.corrot.room.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.adapters.ExercisesListAdapter;
import com.corrot.room.R;
import com.corrot.room.db.entity.Exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity {

    TextView dateTextView;
    RecyclerView exercisesRecyclerView;
    Button addExerciseButton;
    Button saveButton;

    List<ExerciseItem> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        dateTextView = findViewById(R.id.new_workout_date_text_view);
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        addExerciseButton = findViewById(R.id.new_exercise_button);
        saveButton = findViewById(R.id.new_workout_save_button);

        Date date = Calendar.getInstance().getTime();

        // TODO: Do it right way
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
        dateTextView.setText("New workout,\n" + sdf.format(date));

        final ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(this);
        exercisesRecyclerView.setAdapter(exercisesListAdapter);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        exercises = new ArrayList<>();
        exercisesListAdapter.setExercises(exercises);

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ExerciseSetItem> esi = new ArrayList<>();
                exercisesListAdapter.addExercise(new ExerciseItem("New Exercise", esi));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Exercise> exercises = exercisesListAdapter.getExercises();

                // add to DB.
            }
        });
    }
}
