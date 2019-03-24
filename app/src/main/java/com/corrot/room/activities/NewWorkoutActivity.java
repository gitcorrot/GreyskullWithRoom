package com.corrot.room.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.adapters.ExercisesListAdapter;
import com.corrot.room.R;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.viewmodel.ExerciseViewModel;
import com.corrot.room.viewmodel.NewWorkoutViewModel;
import com.corrot.room.viewmodel.WorkoutViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity {

    TextView dateTextView;
    RecyclerView exercisesRecyclerView;
    Button addExerciseButton;
    ImageButton saveWorkoutButton;
    private NewWorkoutViewModel mNewWorkoutViewModel;
    private WorkoutViewModel mWorkoutViewModel;
    private ExerciseViewModel mExerciseViewModel;
    private AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        mActivity = this;

        dateTextView = findViewById(R.id.new_workout_date_text_view);
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        addExerciseButton = findViewById(R.id.new_exercise_button);
        saveWorkoutButton = findViewById(R.id.new_workout_save_button);

        mNewWorkoutViewModel = ViewModelProviders.of(mActivity).get(NewWorkoutViewModel.class);
        mWorkoutViewModel = ViewModelProviders.of(mActivity).get(WorkoutViewModel.class);
        mExerciseViewModel = ViewModelProviders.of(mActivity).get(ExerciseViewModel.class);

        mNewWorkoutViewModel.init();

        final Date date = Calendar.getInstance().getTime();
        dateTextView.setText(MyTimeUtils.parseDate(date,"dd/MM/yyyy"));

        final ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(this);
        exercisesRecyclerView.setAdapter(exercisesListAdapter);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewWorkoutViewModel.getAllExerciseItems().observe(this, new Observer<List<ExerciseItem>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseItem> exerciseItems) {
                exercisesListAdapter.setExercises(exerciseItems);
            }
        });

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExercisesDialog(v.getContext()).show();
            }
        });


        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ExerciseItem> exercises = mNewWorkoutViewModel.getAllExerciseItems().getValue();
                if(exercises != null) {

                    Workout newWorkout = new Workout(date);
                    mWorkoutViewModel.insertSingleWorkout(newWorkout);

                    for(ExerciseItem e : exercises) {

                        // Getting sets of exercise
                        List<ExerciseSetItem> sets = mNewWorkoutViewModel.getSetsByExerciseId(e.id);
                        List<Integer> reps = new ArrayList<>();
                        List<Float> weights = new ArrayList<>();
                        if(sets != null) {
                            for(ExerciseSetItem esi : sets) {
                                reps.add(esi.reps);
                                weights.add(esi.weight);
                            }
                        }
                        Exercise newExercise = new Exercise(newWorkout.id,e.name, weights, reps);
                        mExerciseViewModel.insertSingleExercise(newExercise);
                    }
                }
                mActivity.finishAndRemoveTask();//.finish();
                Toast.makeText(mActivity, "Workout added succesfully!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewWorkoutViewModel.destroyInstance();
    }

    private AlertDialog showExercisesDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Which exercise you want to add?");

        // TODO: get it from shared preferences
        final String[] exercises = {"Squat", "Dead lift", "Bench press"};

        builder.setItems(exercises, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNewWorkoutViewModel.addExercise(new ExerciseItem(exercises[which]));
            }
        });
        return builder.create();
    }
}
