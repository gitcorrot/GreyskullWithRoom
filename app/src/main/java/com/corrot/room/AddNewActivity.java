package com.corrot.room;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewActivity extends AppCompatActivity {

    TextView dateTextView;
    Button saveButton;
    Button addExerciseButton;
    ImageButton deleteExerciseButton;
    FragmentManager fragmentManager;
    List<AddExerciseFragment> exerciseFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        dateTextView = findViewById(R.id.new_workout_date_text_view);
        saveButton = findViewById(R.id.new_workout_save_button);
        addExerciseButton = findViewById(R.id.new_workout_new_exercise_button);

        fragmentManager = getSupportFragmentManager();
        exerciseFragments = null;

        Date date = Calendar.getInstance().getTime();

        // TODO: Do it right way
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
        dateTextView.setText("New workout,\n" + sdf.format(date));

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exerciseFragments.add()
                // TODO: Add list of exercises to choose
                fragmentManager.beginTransaction()
                        .add(R.id.new_workout_linear_layout, new AddExerciseFragment(), "fg").commit();

            }
        });
    }
}
