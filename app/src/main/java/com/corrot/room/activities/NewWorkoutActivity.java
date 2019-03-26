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
import android.util.Log;
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
import com.corrot.room.utils.EntityUtils;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.viewmodel.ExerciseViewModel;
import com.corrot.room.viewmodel.NewWorkoutViewModel;
import com.corrot.room.viewmodel.WorkoutViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewWorkoutActivity extends AppCompatActivity {

    public final static int FLAG_ERROR = -1;
    public final static int FLAG_ADD_WORKOUT = 0;
    public final static int FLAG_UPDATE_WORKOUT = 1;

    TextView dateTextView;
    RecyclerView exercisesRecyclerView;
    Button addExerciseButton;
    ImageButton saveWorkoutButton;

    private NewWorkoutViewModel mNewWorkoutViewModel;
    private WorkoutViewModel mWorkoutViewModel;
    private ExerciseViewModel mExerciseViewModel;

    private AppCompatActivity mActivity;
    private Date date;
    private int currentFlag;
    private Workout mWorkout;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentFlag = extras.getInt("flags", FLAG_ERROR);

            Log.d("NewWorkoutActivity", "Activity opened with flag: " + currentFlag + "!");

            switch (currentFlag) {

                case FLAG_ERROR: {
                    Log.e("NewWorkoutActivity", "Error flag!");
                    break;
                }

                case FLAG_ADD_WORKOUT: {
                    date = Calendar.getInstance().getTime();
                    dateTextView.setText(MyTimeUtils.parseDate(date, "dd/MM/yyyy"));
                    break;
                }

                case FLAG_UPDATE_WORKOUT: {
                    String workoutId = extras.getString("workoutId", "no workout");
                    try {
                        mWorkout = mWorkoutViewModel.getWorkoutById(workoutId);
                        date = mWorkout.workoutDate;
                        dateTextView.setText(MyTimeUtils.parseDate(date, "dd/MM/yyyy"));
                    } catch (InterruptedException e) {
                        Log.d("NewWorkoutActivity", e.getMessage());
                    } catch (ExecutionException e) {
                        Log.d("NewWorkoutActivity", e.getMessage());
                    }

                    if (!workoutId.equals("no workout")) {
                        try {
                            List<Exercise> exercises =
                                    mExerciseViewModel.getExercisesByWorkoutId(workoutId);
                            if (exercises != null) {
                                mNewWorkoutViewModel
                                        .setExercises(EntityUtils.getExerciseItems(exercises));
                                mNewWorkoutViewModel
                                        .setSets(EntityUtils.getExerciseSetItems(exercises));
                            }
                        } catch (InterruptedException e) {
                            Log.d("NewWorkoutActivity", e.getMessage());
                        } catch (ExecutionException e) {
                            Log.d("NewWorkoutActivity", e.getMessage());
                        }
                    }
                    break;
                }
            }
        }

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
                saveWorkout();
                mActivity.finishAndRemoveTask();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to save this workout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveWorkout();
                mActivity.finishAndRemoveTask();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.finishAndRemoveTask();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();
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
        final String[] exercises = {"Squat", "Deadlift", "Bench Press", "Barbel Row",
                                    "Pull-ups", "Head over press"};

        builder.setItems(exercises, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNewWorkoutViewModel.addExercise(new ExerciseItem(exercises[which]));
            }
        });
        return builder.create();
    }

    private void saveWorkout() {
        List<ExerciseItem> exercises = mNewWorkoutViewModel.getAllExerciseItems().getValue();
        if (exercises != null) {

            switch (currentFlag) {

                case FLAG_ADD_WORKOUT:
                    mWorkout = new Workout(date);
                    mWorkoutViewModel.insertSingleWorkout(mWorkout);
                    break;

                case FLAG_UPDATE_WORKOUT:
                    mWorkoutViewModel.updateWorkout(mWorkout);
                    mExerciseViewModel.deleteAllExercisesByWorkoutId(mWorkout.id);
                    break;
            }

            List<Exercise> updatedExercises = new ArrayList<>();

            for (ExerciseItem e : exercises) {

                // Getting sets of exercise
                List<ExerciseSetItem> sets =
                        mNewWorkoutViewModel.getSetsByExercisePosition(e.position);
                List<Integer> reps = new ArrayList<>();
                List<Float> weights = new ArrayList<>();

                if (sets != null) {
                    for (ExerciseSetItem esi : sets) {
                        reps.add(esi.reps);
                        weights.add(esi.weight);
                    }
                }

                Exercise newExercise = new Exercise(mWorkout.id, e.name, weights, reps);

                switch (currentFlag) {

                    case FLAG_ADD_WORKOUT:
                        mExerciseViewModel.insertSingleExercise(newExercise);
                        break;
                    case FLAG_UPDATE_WORKOUT:
                        updatedExercises.add(newExercise);
                        break;
                }
            }

            if (currentFlag == FLAG_UPDATE_WORKOUT) {
                mExerciseViewModel.deleteAllExercisesByWorkoutId(mWorkout.id);
                mExerciseViewModel.insertMultipleExercises(updatedExercises);
            }
        }

        String action = currentFlag == FLAG_ADD_WORKOUT ? "added" : "updated";
        Toast.makeText(mActivity, "Exercise " + action + " successfully!",
                Toast.LENGTH_SHORT).show();
    }
}
